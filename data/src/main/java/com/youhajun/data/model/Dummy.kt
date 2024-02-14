package com.youhajun.data.model

import com.youhajun.data.model.dto.store.PurchaseItem
import com.youhajun.data.model.dto.store.PurchaseItemInfo

object Dummy {
    object Store {

        private fun getPurchaseItems():List<PurchaseItem> {
            val list = mutableListOf<PurchaseItem>()
            for (i in 0..7) {
                list.add(PurchaseItem(
                    i,
                    (i+1)*30,
                    "${(i+1)*3000} 원"
                ))
            }
            return list
        }
    }
}