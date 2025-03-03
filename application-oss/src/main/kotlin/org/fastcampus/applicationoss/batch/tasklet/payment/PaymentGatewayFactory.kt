package org.fastcampus.applicationoss.batch.tasklet.payment

import org.fastcampus.payment.entity.Payment
import org.fastcampus.payment.exception.PaymentException
import org.fastcampus.payment.gateway.PaymentGateway
import org.fastcampus.payment.gateway.PaymentGatewayResponse
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class PaymentGatewayFactory(
    private val applicationContext: ApplicationContext,
    private val environment: Environment,
) {
    fun getPaymentGateway(paymentType: Payment.Type): PaymentGateway {
        val activeProfiles: Array<String>? = environment.activeProfiles

        if (activeProfiles == null || activeProfiles.contains("local") || activeProfiles.contains("test")) {
            return LocalPaymentGateway()
        }

        return when (paymentType) {
            Payment.Type.TOSS_PAY -> applicationContext.getBean("tossPaymentsGateway", PaymentGateway::class.java)
            Payment.Type.PAY_200 -> applicationContext.getBean("pay200Gateway", PaymentGateway::class.java)
            else -> throw PaymentException.NotSupportedPaymentType(paymentType.name)
        }
    }
}

class LocalPaymentGateway : PaymentGateway {
    override fun approve(paymentKey: String, orderId: String, amount: Long): PaymentGatewayResponse {
        return PaymentGatewayResponse(
            status = PaymentGatewayResponse.Status.DONE,
            paymentKey = paymentKey,
            orderId = orderId,
            amount = amount,
        )
    }

    override fun cancel(paymentKey: String, orderId: String, amount: Long): PaymentGatewayResponse {
        return PaymentGatewayResponse(
            status = PaymentGatewayResponse.Status.CANCELED,
            paymentKey = paymentKey,
            orderId = orderId,
            amount = amount,
        )
    }
}
