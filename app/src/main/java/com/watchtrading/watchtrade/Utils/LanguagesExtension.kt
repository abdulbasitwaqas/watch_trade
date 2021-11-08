package com.watchtrading.watchtrade.Utils

import com.watchtrading.watchtrade.Models.Currency


class LanguagesExtension {
    companion object {
        fun MutableList<Currency>.getStringsArray(): List<String> {
            return this.map { it.symbol }
                    .toList()
        }

        fun MutableList<Currency>.getSortedList(): List<String> {
            return this.sortedWith(compareBy { it.symbol })
                    .map { it.symbol }
                    .toList()
        }

    }
}