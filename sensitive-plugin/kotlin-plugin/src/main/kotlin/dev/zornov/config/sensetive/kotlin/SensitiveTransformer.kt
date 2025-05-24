package dev.zornov.config.sensetive.kotlin

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classOrFail
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

class SensitiveTransformer(
    val ctx: IrPluginContext
) : IrElementTransformerVoidWithContext() {

    val sensitiveAnn = FqName("dev.zornov.config.annotations.Sensitive")
    val protectedCid = ClassId.topLevel(FqName("dev.zornov.config.annotations.sensetive.Protected"))
    val cache = mutableMapOf<IrType, IrType>()

    fun IrProperty.isSensitive() = hasAnnotation(sensitiveAnn)

    fun protect(orig: IrType): IrType =
        cache.getOrPut(orig) {
            ctx.referenceClass(protectedCid)
                ?.typeWith(orig)
                ?: error("Class 'Protected' not found")
        }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (!declaration.isSensitive()) return super.visitPropertyNew(declaration)
        val field = declaration.backingField ?: return super.visitPropertyNew(declaration)

        val origType = field.type
        val protType = protect(origType)
        val protCtor = protType.classOrFail.owner.constructors
            .single { it.valueParameters.size == 1 }

        val builder = DeclarationIrBuilder(ctx, declaration.symbol)

        declaration.getter?.let { getter ->
            val dispatchRcvr = getter.dispatchReceiverParameter
                ?: error("Getter does not have a dispatchReceiver")

            val receiverExpr = builder.irGet(dispatchRcvr)
            val getFieldExpr = builder.irGetField(
                receiver = receiverExpr,
                field    = field
            )

            getter.body = builder.irExprBody(
                builder.irCall(protCtor).apply {
                    putTypeArgument(0, origType)
                    putValueArgument(0, getFieldExpr)
                    type = protType
                }
            )
            getter.returnType = protType
        }

        declaration.setter?.let { setter ->
            val dispatchRcvr = setter.dispatchReceiverParameter
                ?: error("Setter does not have a dispatchReceiver")
            val param = setter.valueParameters.single()

            val valueField = protType.classOrFail.owner
                .properties
                .single { it.name.asString() == "value" }
                .backingField
                ?: error("Protected<T> must have a `value` field")

            setter.body = builder.irBlockBody {
                val rawExpr = builder.irGetField(
                    receiver = builder.irGet(param),
                    field    = valueField
                )

                +irSetField(
                    receiver = irGet(dispatchRcvr),
                    field    = field,
                    value    = rawExpr
                )
                +irReturn(irUnit())
            }

            setter.valueParameters.single().type = protType
        }

        return super.visitPropertyNew(declaration)
    }

}
