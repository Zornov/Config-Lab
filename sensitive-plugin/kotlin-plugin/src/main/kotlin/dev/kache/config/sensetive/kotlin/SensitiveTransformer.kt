package dev.kache.config.sensetive.kotlin

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irExprBody
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrGetField
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classOrFail
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

class SensitiveTransformer(
    val pluginContext: IrPluginContext
) : IrElementTransformerVoidWithContext() {

    val sensitiveFqName = FqName("dev.kache.config.annotations.Sensitive")
    val protectedClassId =
        ClassId.topLevel(FqName("dev.kache.config.annotations.sensetive.Protected"))

    val cache = hashMapOf<IrType, IrType>()

    fun IrProperty.isSensitive() = hasAnnotation(sensitiveFqName)
    fun protected(original: IrType): IrType =
        cache.getOrPut(original) {
            val sym: IrClassSymbol =
                pluginContext.referenceClass(protectedClassId)
                    ?: error("Class Protected not found")
            sym.typeWith(original)
        }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitPropertyNew(declaration: IrProperty): IrStatement {
        if (!declaration.isSensitive()) return super.visitPropertyNew(declaration)

        val field = declaration.backingField ?: return super.visitPropertyNew(declaration)
        val origType = field.type
        val protType = protected(origType)

        field.type = protType
        declaration.getter?.returnType = protType
        declaration.setter?.valueParameters?.singleOrNull()?.type = protType

        val builder = DeclarationIrBuilder(pluginContext, declaration.symbol)
        val ctor = protType.classOrFail.owner.constructors
            .single { it.valueParameters.size == 1 }

        val oldInit = field.initializer?.expression
            ?: error("@Sensitive property '${declaration.name}' must have initializer")

        field.initializer = builder.irExprBody(
            builder.irCall(ctor).apply {
                putTypeArgument(0, origType)
                putValueArgument(0, oldInit)
                type = protType
            }
        )

        return super.visitPropertyNew(declaration)
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitGetField(expression: IrGetField): IrExpression {
        val prop = expression.symbol.owner.correspondingPropertySymbol?.owner
        if (prop?.isSensitive() == true) {
            expression.type = protected(prop.backingField!!.type)
        }
        return super.visitGetField(expression)
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitCall(expression: IrCall): IrExpression {
        val funOwner = expression.symbol.owner
        val prop = funOwner.correspondingPropertySymbol?.owner
        if (prop?.isSensitive() == true) {
            expression.type = protected(prop.backingField!!.type)
        }
        return super.visitCall(expression)
    }

}
