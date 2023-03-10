package com.muxiyun.bika.adapter

import com.muxiyun.bika.R
import com.muxiyun.bika.base.BaseBindingAdapter
import com.muxiyun.bika.base.BaseBindingHolder
import com.muxiyun.bika.bean.KnightBean
import com.muxiyun.bika.databinding.ItemKnightBinding
import com.muxiyun.bika.utils.GlideApp
import com.muxiyun.bika.utils.GlideUrlNewKey

class KnightAdapter : BaseBindingAdapter<KnightBean.Users, ItemKnightBinding>(R.layout.item_knight) {

    override fun bindView(
        holder: BaseBindingHolder<*, *>,
        bean: KnightBean.Users,
        binding: ItemKnightBinding,
        position: Int
    ) {
        //头像
        GlideApp.with(holder.itemView)
            .load(
                if (bean.avatar != null)
                    GlideUrlNewKey(bean.avatar.fileServer, bean.avatar.path)
                else
                    R.drawable.placeholder_avatar_2
            )
            .placeholder(R.drawable.placeholder_avatar_2)
            .into(binding.knightUserImage)
        //头像框
        GlideApp.with(holder.itemView)
            .load(if (bean.character.isNullOrEmpty()) "" else bean.character)
            .into(binding.knightUserCharacter)

        holder.addOnClickListener(R.id.knight_user_image_layout)
    }
}