package com.example.myshop.activities.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import com.example.myshop.R
import com.example.myshop.databinding.FragmentDeleteDialogBinding


class DeleteDialogFragment : DialogFragment() {
    private lateinit var binding : FragmentDeleteDialogBinding
    private var title : String = ""

    private var eventDeleteProductListener : (()->Unit)? = null

    fun setEventDeleteProductListener(listener : (()->Unit)){
        eventDeleteProductListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeleteDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels*0.8).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.apply {
            this!!.requestFeature(Window.FEATURE_NO_TITLE)
            this!!.setBackgroundDrawableResource(android.R.color.transparent)
            this!!.setGravity(Gravity.CENTER)
        }
        return dialog
    }

    private fun initUI(){
        binding.delete.setOnClickListener {
            eventDeleteProductListener?.invoke()
            dialog?.dismiss()
        }
        binding.cancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DeleteDialogFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}