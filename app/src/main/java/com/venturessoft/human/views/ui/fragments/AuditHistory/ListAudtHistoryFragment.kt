package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentListAudtHistoryBinding
import com.venturessoft.human.models.ListAuditHistoryModel
import com.venturessoft.human.models.request.DetallesRequest
import com.venturessoft.human.models.response.DetallesResponse
import com.venturessoft.human.models.response.ListaAuditoriaResponse
import com.venturessoft.human.models.response.itemAuditoria
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.adapters.AdapterListAuditHistory
import com.venturessoft.human.views.ui.viewModels.DetalleAuditHistoryViewModel

class ListAudtHistoryFragment : Fragment() {
    private lateinit var binding: FragmentListAudtHistoryBinding
    var detalleAuditHistoryViewModel = DetalleAuditHistoryViewModel()
    private var mListener: OnFragmentInteractionListener? = null
    private var auditoria: ArrayList<itemAuditoria>? = null
    private var listaAuditoriaResponse = ListaAuditoriaResponse()
    private var adapter: AdapterListAuditHistory? = null
    private var mainInterface: MainInterface? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        lateinit var listAuditHistoryModel: ArrayList<ListAuditHistoryModel>
        var detallesResponse = DetallesResponse()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListAudtHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.details_audit_historial_title))
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
    private fun initView() {
        AuditHistoryFilterFragment
        mListener?.onBack("BackFilter")
        loadData()
        listenButtons()
        styleRecycler()
        addObservers()
    }

    private fun addObservers() {
        detalleAuditHistoryViewModel.detalleAuditMutableData.observe(viewLifecycleOwner) {
            detallesResponse = it
            if (detallesResponse.codigo!! == "ET000") {
                if (detallesResponse.auditoriaDet != null) {
                    when (detallesResponse.auditoriaDet!![0].codigoEtime) {
                        "ET029" -> loadFragment(ET029Fragment())
                        "DB233", "ET006", "ET027", "ET032", "ET265", "ET318", "ET323" -> loadFragment(
                            ET032Fragment()
                        )

                        "ET000" -> loadFragment(ItemCheckInOutSuccessFragment())
                        "ET030", "ET507" -> loadFragment(ET030Fragment())
                        else -> loadFragment(ItemFailsGeneralErrorFragment())
                    }
                } else {
                    Utilities.loadMessageError("ET327", requireActivity(), childFragmentManager)
                }
            } else {
                Utilities.loadMessageError(
                    detallesResponse.codigo as String,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        detalleAuditHistoryViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
    }

    private fun styleRecycler() {
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_recicler
            )!!
        )
        binding.auditHistory.addItemDecoration(itemDecorator)
        binding.auditHistory.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireActivity())
        loadReciclerView()
    }

    private fun loadReciclerView() {
        val list = java.util.ArrayList<ListAuditHistoryModel>()
        for (i in 0 until listaAuditoriaResponse.auditoria!!.size) {
            val listAuditHistoryModel = ListAuditHistoryModel()
            listAuditHistoryModel.setIds(listaAuditoriaResponse.auditoria!![i].id.toString())
            listAuditHistoryModel.setNumCia(listaAuditoriaResponse.auditoria!![i].numCia!!)
            listAuditHistoryModel.setNumEmp(listaAuditoriaResponse.auditoria!![i].numEmp!!)
            listAuditHistoryModel.setFechaChecadaa(listaAuditoriaResponse.auditoria!![i].fechaChecada!!)
            listAuditHistoryModel.setTipoo(listaAuditoriaResponse.auditoria!![i].tipo!!)
            listaAuditoriaResponse.auditoria!![i].codigoEtime
            if (listaAuditoriaResponse.auditoria!![i].codigoEtime!! != "null") {
                listAuditHistoryModel.setCodigoEtimee(listaAuditoriaResponse.auditoria!![i].codigoEtime!!)
            }
            list.add(listAuditHistoryModel)
        }
        listAuditHistoryModel = list
        adapter =
            AdapterListAuditHistory(requireActivity().applicationContext, listAuditHistoryModel)
        binding.auditHistory.adapter = adapter

    }

    private fun loadData() {
        listaAuditoriaResponse = AuditHistoryFilterFragment.listaAuditoriaResponse
        if (arguments != null) {
            auditoria =
                requireArguments().getSerializable("listaAuditoriaResponse") as ArrayList<itemAuditoria>?
        }
    }

    private fun listenButtons() {
        binding.btnBackFilterAuditHistory.setOnClickListener {
            when (User.ambiente) {
                "HU" -> {
                    loadFragment(AuditHistoryFilterFragment())
                }

                "ET" -> {
                    loadFragment(AuditHistoryFilterFragment())
                }
            }
        }
        binding.auditHistory.addOnItemTouchListener(
            RecyclerTouchListener(
                requireActivity().applicationContext, binding.auditHistory,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val numCiaTextView = view.findViewById<TextView>(R.id.numCia)
                        val idEmpTextView = view.findViewById<TextView>(R.id.idEmp)
                        val idEmpNumTextView = view.findViewById<TextView>(R.id.employeeNumber)
                        if (numCiaTextView.text.toString().trim()
                                .isNotEmpty() && idEmpTextView.text.toString().trim()
                                .isNotEmpty() && idEmpNumTextView.text.toString().trim()
                                .isNotEmpty()
                        ) {
                            val numCiaText = numCiaTextView.text.trim().toString().toLong()
                            val idEmpText = idEmpTextView.text.trim().toString()
                            val idEmpNum = idEmpNumTextView.text.trim().toString().toLong()
                            val detallesRequest = DetallesRequest(
                                numCia = numCiaText,
                                id = idEmpText,
                                numEmp = idEmpNum
                            )
                            detalleAuditHistoryViewModel.getDetalleAuditHistory(detallesRequest)
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {
                    }
                }
            )
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    private fun loadFragment(fragmet: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
            .commit()
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(
        context: Context,
        recyclerView: RecyclerView,
        private val clickListener: ClickListener?
    ) : RecyclerView.OnItemTouchListener {
        private val gestureDetector: GestureDetector

        init {
            gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }

                    override fun onLongPress(e: MotionEvent) {
                        val child = recyclerView.findChildViewUnder(e.x, e.y)
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                        }
                    }
                })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }

    fun showLoadingAnimation() {
        binding.loadingAnimation.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimation.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


}