package com.ta.dodo.ui.main.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ta.dodo.R
import com.ta.dodo.model.wallet.TransactionHistory
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class TransactionsHistoryAdapter(
    private var transactions: List<TransactionHistory>,
    private val publicKey: String
) :
    RecyclerView.Adapter<TransactionsHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_history_view, parent, false) as View
        return ViewHolder(view, publicKey)
    }

    override fun getItemCount() = transactions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    fun addAll(transactions: List<TransactionHistory>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, val publicKey: String) : RecyclerView.ViewHolder(view) {
        private var transactionContext: TextView = view.findViewById(R.id.tv_context_transaction)
        private var transactionAmount: TextView = view.findViewById(R.id.tv_amount_transaction)
        private var transactionDate: TextView = view.findViewById(R.id.tv_date_transaction)

        fun bind(transaction: TransactionHistory) {
            transaction.let {
                val isReceiver = publicKey == it.to

                val amount = formatAmount(it.amount.toFloat().toInt(), isReceiver)
                val date = formatDate(it.date)
                val context = formatContext(it.from, it.to, isReceiver)

                setData(amount, date, context, isReceiver)
            }
        }

        private fun setData(amount: String, date: String, context: String, isReceiver: Boolean) {
            transactionAmount.text = amount
            transactionContext.text = context
            transactionDate.text = date

            if (isReceiver) {
                val color = Color.parseColor("#303F9F")
                transactionAmount.setTextColor(color)
            } else {
                val color = Color.parseColor("#E91E63")
                transactionAmount.setTextColor(color)
            }
        }

        private fun formatAmount(amount: Int, isReceiver: Boolean): String {
            val numberFormat = DecimalFormat()
            return when (isReceiver) {
                true -> "Rp${numberFormat.format(amount)}"
                false -> "-Rp${numberFormat.format(amount)}"
            }
        }

        private fun formatDate(dateString: String): String {
            var instant = Instant.parse(dateString)
            instant = instant.plusSeconds(3600 * 8)

            val formatter = DateTimeFormatter
                .ofPattern("dd MMM, hh:mm")
                .withZone(ZoneId.of("UTC"))
            return formatter.format(instant)
        }

        private fun formatContext(from: String, to: String, isReceiver: Boolean): String {
            return when (isReceiver) {
                true -> "from ${from.subSequence(0, 10)}..."
                false -> "to ${to.subSequence(0, 10)}..."
            }
        }
    }
}