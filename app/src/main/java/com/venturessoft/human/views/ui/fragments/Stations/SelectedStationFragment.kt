package com.venturessoft.human.views.ui.fragments.Stations


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentSelectedStationBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.utils.Preferences
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.ui.fragments.Employe.EditEmployeeFragment
import com.venturessoft.human.views.ui.fragments.Employe.RegisterEmployeeFragment

class SelectedStationFragment : DialogFragment(){

    companion object {
        lateinit var myContext: Activity
        var redirect: String = ""
        var nameEmploye: String? = ""
        var apPaterno: String? = ""
        var apMaterno: String? = ""
        var nEmpleadp: String? = null
        var itemsSeleccionados = ArrayList<Int>()
        private const val ARG_NAME_EMPLOYEE = "nameEmployee"
        private const val ARG_APPATERNO = "apPaterno"
        private const val ARG_APMATERNO = "apMaterno"
        private const val ARG_NEMPLEADO = "nEmpleado"
        private const val ARG_ARRAYDATA = "arrayData"
        lateinit var myDataBase: DBDemo
        private const val ARG_USER = "userInfo"
        lateinit var recicler: androidx.recyclerview.widget.RecyclerView
        fun newInstance(loggedUser: User, nameEmployeee: String, apPaternoo: String, apMaternoo: String, nEmpleado: String): SelectedStationFragment {
            val fragment = SelectedStationFragment()

            val args = Bundle()
            args.putSerializable(ARG_USER, loggedUser)
            // args.putSerializable(ARG_USER, loggedUser)
            args.putSerializable(ARG_NAME_EMPLOYEE, nameEmployeee)
            args.putSerializable(ARG_APPATERNO, apPaternoo)
            args.putSerializable(ARG_APMATERNO, apMaternoo)
            args.putSerializable(ARG_NEMPLEADO, nEmpleado)
            args.putSerializable(ARG_ARRAYDATA, itemsSeleccionados)
            fragment.arguments = args
            return fragment
        }
    }

    private var mListener: OnFragmentInteractionListener? = null
    var user: User? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    private val TAG: String = SelectedStationFragment::class.java.simpleName
    private var _binding : FragmentSelectedStationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentSelectedStationBinding.inflate(inflater, container, false)
        Log.d(TAG, "ON CREATE VIEW")
        // Inflate the layout for this fragment
        user = Preferences.getUser(requireActivity().applicationContext)
        //Busca en la bd por mantenimiento de empleado
        myDataBase.getPorEstaciones()
        //Carga del reciclerview
        loadReciclerData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        loadSaveData()


        //Datos del usuario
        user = Preferences.getUser(requireActivity().applicationContext)
        //Oyente presiona el boton atras
        mListener?.onBack("backSelectionEmployee")

        binding.btnBackSelectedRegister.setOnClickListener {
            requireActivity().onBackPressed()
            if (redirect == "edit") {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, EditEmployeeFragment.newInstance(user!!, nameEmploye, apPaterno, apMaterno, nEmpleadp, null), "RegisterEmployee")
                    .commit()
            }else
            {
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, RegisterEmployeeFragment.newInstance( nameEmploye, apPaterno, apMaterno, nEmpleadp, null), "RegisterEmployee")
                        .commit()
            }
        }
        binding.saveSelected.setOnClickListener {
        //    println("PopBakStack")
            requireActivity().supportFragmentManager.isDestroyed
            //   activity!!.fragmentManager.popBackStack()
            //      activity!!.onBackPressed()

            //     activity!!.finish()

        //    println("Redirecciona a : "+ redirect)
            if (redirect == "edit") {
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, EditEmployeeFragment.newInstance(user!!, nameEmploye, apPaterno, apMaterno, nEmpleadp, null), "RegisterEmployee")
                        .commit()

            } else {
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, RegisterEmployeeFragment.newInstance( nameEmploye, apPaterno, apMaterno, nEmpleadp, null), "RegisterEmployee")
                        .commit()
            }
        }
    }

    private fun loadSaveData() {
        binding.saveSelected.isEnabled = itemsSeleccionados.isNotEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myDataBase = DBDemo(requireActivity())
        myContext = requireActivity()
        nameEmploye = requireArguments().getSerializable(ARG_NAME_EMPLOYEE) as? String
       // println("El nombre del empleado es: " + nameEmploye)
        apPaterno = requireArguments().getSerializable(ARG_APPATERNO) as? String
    //    println("El nombre del empleado es: " + apPaterno)
        apMaterno = requireArguments().getSerializable(ARG_APMATERNO) as? String
        nEmpleadp = requireArguments().getSerializable(ARG_NEMPLEADO) as? String
        itemsSeleccionados = requireArguments().getSerializable(ARG_ARRAYDATA) as ArrayList<Int>
    }

    private fun loadReciclerData() {
        binding.selectEstaciones.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(requireActivity(), androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
        binding.selectEstaciones.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireActivity())
       /* val simpleAdapter = SimpleAdapterEstacionesSelect((0..0).map { "Item: $it" }.toMutableList())


        while (res.moveToNext()) {

            println("Posicion 0 :" + res.getString(0))
            println("Posicion 1 :" + res.getString(1))
            println("Posicion 2 :" + res.getString(7))
            var datos: String = res.getString(0) + "," + res.getString(1) + "," + res.getString(7)
            simpleAdapter.addItem(datos)

        }
        recicler.adapter = simpleAdapter
        var ultimo = simpleAdapter.itemCount.toInt() - 1
        simpleAdapter.removeAt(ultimo)*/

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    fun addStation(position: Int) {
        println("Filtro:" + itemsSeleccionados.contains(position))
        itemsSeleccionados.add(position)
       val saveData = myContext.findViewById<Button>(R.id.saveSelected)
        saveData.isEnabled = true
    }

    fun emptyStatation() {
        val saveData = myContext.findViewById<Button>(R.id.saveSelected)
        println("Item" + itemsSeleccionados.size)
        saveData.isEnabled = itemsSeleccionados.size != 0

    }

}
