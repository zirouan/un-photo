package com.zirouan.unphoto.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zirouan.unphoto.util.OnItemClickListener

@Suppress("UNCHECKED_CAST")
abstract class BaseViewAdapter<T, VB : ViewBinding> :
        RecyclerView.Adapter<BaseViewAdapter<T, VB>.ViewHolder>() {

    private val mDataList: MutableList<T> by lazy { mutableListOf() }

    abstract val bindingInflater: (LayoutInflater) -> VB

    private var mIsLoading = false
    private var mLoadingPosition = RecyclerView.NO_POSITION
    private var mOnItemClickListener: OnItemClickListener<T>? = null

    var onItemClickListener
        get() = mOnItemClickListener
        set(value) {
            mOnItemClickListener = value
        }

    inner class ViewHolder(val binding: VB) :
            RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(bindingInflater.invoke(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.onBindViewHolderBase(holder, position)

        holder.itemView.setOnClickListener {
            mOnItemClickListener?.invoke(
                    it, holder.adapterPosition,
                    getItem(holder.adapterPosition)
            )
        }
    }

    abstract fun onBindViewHolderBase(holder: ViewHolder?, position: Int)

    override fun getItemCount() = mDataList.size

    protected fun getItem(index: Int) = mDataList[index]

    val data: MutableList<T>
        get() = mDataList

    val lastIndex: Int
        get() = mDataList.lastIndex

    val isEmpty: Boolean
        get() = data.isEmpty()

    fun getItemRange(startIndex: Int) = getItemRange(startIndex, mDataList.size)

    fun getItemRange(startIndex: Int, endIndex: Int) = mDataList.subList(startIndex, endIndex)

    fun addItem(item: T) {
        mDataList.add(item)
        notifyItemInserted(if (itemCount > 0) mDataList.lastIndex else 0)
    }

    fun addItem(position: Int, item: T) {
        mDataList.add(position, item)
        notifyItemInserted(position)
    }

    fun setItem(position: Int, item: T?) {
        if (item == null) {
            return
        }

        if (position > RecyclerView.NO_POSITION) {
            mDataList[position] = item
        } else {
            mDataList.add(item)
        }
        notifyDataSetChanged()
    }

    fun changeItem(position: Int, item: T?) {
        item?.let {
            mDataList[position] = item
            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        if (mDataList.isEmpty()) {
            return
        }

        this.mDataList.removeAt(position)
        this.notifyItemRemoved(position)
    }

    fun clearData() {
        this.mDataList.clear()
        this.notifyDataSetChanged()
    }

    fun addData(list: List<T>) {
        val firstItemPosition = this.mDataList.lastIndex + 1
        this.mDataList.addAll(list)
        this.notifyItemRangeInserted(firstItemPosition, list.size)
    }

    fun addData(position: Int, list: List<T>) {
        this.mDataList.addAll(position, list)
        this.notifyItemRangeInserted(position, list.size)
    }

    fun removeData(list: List<T>) {
        this.mDataList.removeAll(list)
        this.notifyDataSetChanged()
    }

    fun showLoading(showInBottom: Boolean) {
        if (itemCount == 0 || mIsLoading) {
            return
        }

        mIsLoading = true

        mLoadingPosition = if (showInBottom) {
            addItem(getItem(0))
            lastIndex
        } else {
            addItem(0, getItem(0))
            0
        }
    }

    fun hideLoading() {
        if (itemCount == 0 || !mIsLoading || mLoadingPosition == RecyclerView.NO_POSITION) {
            return
        }

        mIsLoading = false

        mDataList.removeAt(mLoadingPosition)
        notifyItemRemoved(mLoadingPosition)

        mLoadingPosition = RecyclerView.NO_POSITION
    }

    fun isLoading(): Boolean {
        return mIsLoading
    }
}