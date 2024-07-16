package com.venturessoft.human.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


data class CountryPhone(
    val nombre: String,
    val iso3: String,
    val cveTelefono: String,
    val imagen: String
)

class AlgorithmAdapter(context: Context, private val algorithmList: List<CountryPhone>) : ArrayAdapter<CountryPhone>(context, 0, algorithmList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }
    override fun getCount() = algorithmList.size
    override fun getItem(position: Int) = algorithmList[position]
    override fun getItemId(position: Int) = position.toLong()
    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var newConvertView = convertView
        if (newConvertView == null) {
            newConvertView = LayoutInflater.from(context).inflate(com.venturessoft.human.R.layout.item_country_phone, parent, false)
        }
        val textViewName = newConvertView?.findViewById<TextView>(com.venturessoft.human.R.id.nameCountry)
        val textCodeCountry = newConvertView?.findViewById<TextView>(com.venturessoft.human.R.id.codeCountry)
        val currentItem: CountryPhone = getItem(position)

        textViewName?.text = currentItem.nombre
        textCodeCountry?.text = currentItem.cveTelefono
        return newConvertView!!
    }
}

class CountriesNumber {

    fun getCountries(): List<CountryPhone> {
        return listOf(
            CountryPhone(
                "México",
                "MEX",
                "+52",
                "https://flagsapi.com/MX/shiny/64.png",
            ), CountryPhone(
                "Afganistán",
                "AFG",
                "+93",
                "https://flagsapi.com/AE/shiny/64.png"
            ), CountryPhone(
                "Albania",
                "ALB",
                "+355",
                "https://flagsapi.com/AL/shiny/64.png"
            ), CountryPhone(
                "Alemania",
                "DEU",
                "+49",
                "https://flagsapi.com/DE/shiny/64.png"
            ), CountryPhone(
                "Andorra",
                "AND",
                "+376",
                "https://flagsapi.com/AD/shiny/64.png"
            ), CountryPhone(
                "Angola",
                "AGO",
                "+244",
                "https://flagsapi.com/AO/shiny/64.png"
            ), CountryPhone(
                "Anguila",
                "AIA",
                "+1264",
                "https://flagsapi.com/AI/shiny/64.png"
            ), CountryPhone(
                "Antártida",
                "ATA",
                "+672",
                "https://flagsapi.com/AQ/shiny/64.png"
            ), CountryPhone(
                "Antigua y Barbuda",
                "ATG",
                "+1268",
                "https://flagsapi.com/AG/shiny/64.png",
            ), CountryPhone(
                "Arabia Saudita",
                "SAU",
                "+966",
                "https://flagsapi.com/SA/shiny/64.png"
            ), CountryPhone(
                "Argelia",
                "DZA",
                "+213",
                "https://flagsapi.com/DZ/shiny/64.png"
            ), CountryPhone(
                "Argentina",
                "ARG",
                "+54",
                "https://flagsapi.com/AR/shiny/64.png"
            ), CountryPhone(
                "Armenia",
                "ARM",
                "+374",
                "https://flagsapi.com/AM/shiny/64.png"
            ), CountryPhone(
                "Aruba",
                "ABW",
                "+297",
                "https://flagsapi.com/AW/shiny/64.png"
            ), CountryPhone(
                "Australia",
                "AUS",
                "+61",
                "https://flagsapi.com/AU/shiny/64.png"
            ), CountryPhone(
                "Austria",
                "AUT",
                "+43",
                "https://flagsapi.com/AT/shiny/64.png"
            ), CountryPhone(
                "Azerbaiyán",
                "AZE",
                "+994",
                "https://flagsapi.com/AZ/shiny/64.png"
            ), CountryPhone(
                "Bélgica",
                "BEL",
                "+32",
                "https://flagsapi.com/BE/shiny/64.png"
            ), CountryPhone(
                "Bahamas",
                "BHS",
                "+1242",
                "https://flagsapi.com/BS/shiny/64.png"
            ), CountryPhone(
                "Bahrein",
                "BHR",
                "+973",
                "https://flagsapi.com/BH/shiny/64.png"
            ), CountryPhone(
                "Bangladesh",
                "BGD",
                "+880",
                "https://flagsapi.com/BD/shiny/64.png"
            ), CountryPhone(
                "Barbados",
                "BRB",
                "+1246",
                "https://flagsapi.com/BB/shiny/64.png"
            ), CountryPhone(
                "Belice",
                "BLZ",
                "+501",
                "https://flagsapi.com/BZ/shiny/64.png"
            ), CountryPhone(
                "Benín",
                "BEN",
                "+229",
                "https://flagsapi.com/BJ/shiny/64.png"
            ), CountryPhone(
                "Bhután",
                "BTN",
                "+975",
                "https://flagsapi.com/BT/shiny/64.png"
            ), CountryPhone(
                "Bielorrusia",
                "BLR",
                "+375",
                "https://flagsapi.com/BY/shiny/64.png"
            ), CountryPhone(
                "Birmania",
                "MMR",
                "+95",
                "https://flagsapi.com/MM/shiny/64.png"
            ), CountryPhone(
                "Bolivia",
                "BOL",
                "+591",
                "https://flagsapi.com/BO/shiny/64.png"
            ), CountryPhone(
                "Bosnia y Herzegovina",
                "BIH",
                "+387",
                "https://flagsapi.com/BA/shiny/64.png"
            ), CountryPhone(
                "Botsuana",
                "BWA",
                "+267",
                "https://flagsapi.com/BW/shiny/64.png"
            ), CountryPhone(
                "Brasil",
                "BRA",
                "+55",
                "https://flagsapi.com/BR/shiny/64.png"
            ), CountryPhone(
                "Brunéi",
                "BRN",
                "+673",
                "https://flagsapi.com/BN/shiny/64.png"
            ), CountryPhone(
                "Bulgaria",
                "BGR",
                "+359",
                "https://flagsapi.com/BG/shiny/64.png"
            ), CountryPhone(
                "Burkina Faso",
                "BFA",
                "+226",
                "https://flagsapi.com/BF/shiny/64.png"
            ), CountryPhone(
                "Burundi",
                "BDI",
                "+257",
                "https://flagsapi.com/BI/shiny/64.png"
            ), CountryPhone(
                "Cabo Verde",
                "CPV",
                "+238",
                "https://flagsapi.com/CV/shiny/64.png"
            ), CountryPhone(
                "Camboya",
                "KHM",
                "+855",
                "https://flagsapi.com/KH/shiny/64.png"
            ), CountryPhone(
                "Camerún",
                "CMR",
                "+237",
                "https://flagsapi.com/KM/shiny/64.png"
            ), CountryPhone(
                "Canadá",
                "CAN",
                "+1",
                "https://flagsapi.com/CA/shiny/64.png"
            ), CountryPhone(
                "Chad",
                "TCD",
                "+235",
                "https://flagsapi.com/TD/shiny/64.png"
            ), CountryPhone(
                "Chile",
                "CHL",
                "+56",
                "https://flagsapi.com/CL/shiny/64.png"
            ), CountryPhone(
                "China",
                "CHN",
                "+86",
                "https://flagsapi.com/CN/shiny/64.png"
            ), CountryPhone(
                "Chipre",
                "CYP",
                "+357",
                "https://flagsapi.com/CY/shiny/64.png"
            ), CountryPhone(
                "Ciudad del Vaticano",
                "VAT",
                "+39",
                "https://flagsapi.com/VA/shiny/64.png"
            ), CountryPhone(
                "Colombia",
                "COL",
                "+57",
                "https://flagsapi.com/CO/shiny/64.png"
            ), CountryPhone(
                "Comoras",
                "COM",
                "+269",
                "https://flagsapi.com/KM/shiny/64.png"
            ), CountryPhone(
                "República del Congo",
                "COG",
                "+242",
                "https://flagsapi.com/CG/shiny/64.png"
            ), CountryPhone(
                "República Democrática del Congo",
                "COD",
                "+243",
                "https://flagsapi.com/CD/shiny/64.png"
            ), CountryPhone(
                "Corea del Norte",
                "PRK",
                "+850",
                "https://flagsapi.com/KP/shiny/64.png"
            ), CountryPhone(
                "Corea del Sur",
                "KOR",
                "+82",
                "https://flagsapi.com/KR/shiny/64.png"
            ), CountryPhone(
                "Costa de Marfil",
                "CIV",
                "+225",
                "https://flagsapi.com/CV/shiny/64.png"
            ), CountryPhone(
                "Costa Rica",
                "CRI",
                "+506",
                "https://flagsapi.com/CR/shiny/64.png"
            ), CountryPhone(
                "Croacia",
                "HRV",
                "+385",
                "https://flagsapi.com/HR/shiny/64.png"
            ), CountryPhone(
                "Cuba",
                "CUB",
                "+53",
                "https://flagsapi.com/CU/shiny/64.png"
            ), CountryPhone(
                "Curazao",
                "CWU",
                "+5999",
                "https://flagsapi.com/CW/shiny/64.png"
            ), CountryPhone(
                "Dinamarca",
                "DNK",
                "+45",
                "https://flagsapi.com/DK/shiny/64.png"
            ), CountryPhone(
                "Dominica",
                "DMA",
                "+1767",
                "https://flagsapi.com/DM/shiny/64.png"
            ), CountryPhone(
                "Ecuador",
                "ECU",
                "+593",
                "https://flagsapi.com/EC/shiny/64.png"
            ), CountryPhone(
                "Egipto",
                "EGY",
                "+20",
                "https://flagsapi.com/EG/shiny/64.png"
            ), CountryPhone(
                "El Salvador",
                "SLV",
                "+503",
                "https://flagsapi.com/SV/shiny/64.png"
            ), CountryPhone(
                "Emiratos Árabes Unidos",
                "ARE",
                "+971",
                "https://flagsapi.com/AE/shiny/64.png"
            ), CountryPhone(
                "Eritrea",
                "ERI",
                "+291",
                "https://flagsapi.com/ER/shiny/64.png"
            ), CountryPhone(
                "Eslovaquia",
                "SVK",
                "+421",
                "https://flagsapi.com/SK/shiny/64.png"
            ), CountryPhone(
                "Eslovenia",
                "SVN",
                "+386",
                "https://flagsapi.com/SI/shiny/64.png"
            ), CountryPhone(
                "España",
                "ESP",
                "+34",
                "https://flagsapi.com/ES/shiny/64.png"
            ), CountryPhone(
                "Estados Unidos de América",
                "USA",
                "+1",
                "https://flagsapi.com/US/shiny/64.png"
            ), CountryPhone(
                "Estonia",
                "EST",
                "+372",
                "https://flagsapi.com/EE/shiny/64.png"
            ), CountryPhone(
                "Etiopía",
                "ETH",
                "+251",
                "https://flagsapi.com/ET/shiny/64.png"
            ), CountryPhone(
                "Filipinas",
                "PHL",
                "+63",
                "https://flagsapi.com/PH/shiny/64.png"
            ), CountryPhone(
                "Finlandia",
                "FIN",
                "+358",
                "https://flagsapi.com/FI/shiny/64.png"
            ), CountryPhone(
                "Fiyi",
                "FJI",
                "+679",
                "https://flagsapi.com/FJ/shiny/64.png"
            ), CountryPhone(
                "Francia",
                "FRA",
                "+33",
                "https://flagsapi.com/FR/shiny/64.png"
            ), CountryPhone(
                "Gabón",
                "GAB",
                "+241",
                "https://flagsapi.com/GA/shiny/64.png"
            ), CountryPhone(
                "Gambia",
                "GMB",
                "+220",
                "https://flagsapi.com/GM/shiny/64.png"
            ), CountryPhone(
                "Georgia",
                "GEO",
                "+995",
                "https://flagsapi.com/GE/shiny/64.png",
            ), CountryPhone(
                "Ghana",
                "GHA",
                "+233",
                "https://flagsapi.com/GH/shiny/64.png"
            ), CountryPhone(
                "Gibraltar",
                "GIB",
                "+350",
                "https://flagsapi.com/GI/shiny/64.png"
            ), CountryPhone(
                "Granada",
                "GRD",
                "+1473",
                "https://flagsapi.com/GD/shiny/64.png"
            ), CountryPhone(
                "Grecia",
                "GRC",
                "+30",
                "https://flagsapi.com/GR/shiny/64.png"
            ), CountryPhone(
                "Groenlandia",
                "GRL",
                "+299",
                "https://flagsapi.com/GL/shiny/64.png"
            ), CountryPhone(
                "Guam",
                "GUM",
                "+1671",
                "https://flagsapi.com/GU/shiny/64.png"
            ), CountryPhone(
                "Guatemala",
                "GTM",
                "+502",
                "https://flagsapi.com/GT/shiny/64.png"
            ), CountryPhone(
                "Guayana Francesa",
                "GUF",
                "+594",
                "https://flagsapi.com/FR/shiny/64.png"
            ), CountryPhone(
                "Guernsey",
                "GGY",
                "+44",
                "https://flagsapi.com/GG/shiny/64.png"
            ), CountryPhone(
                "Guinea",
                "GIN",
                "+224",
                "https://flagsapi.com/GN/shiny/64.png"
            ), CountryPhone(
                "Guinea Ecuatorial",
                "GNQ",
                "+240",
                "https://flagsapi.com/GQ/shiny/64.png"
            ), CountryPhone(
                "Guinea-Bissau",
                "GNB",
                "+245",
                "https://flagsapi.com/GW/shiny/64.png"
            ), CountryPhone(
                "Guyana",
                "GUY",
                "+592",
                "https://flagsapi.com/GY/shiny/64.png"
            ), CountryPhone(
                "Haití",
                "HTI",
                "+509",
                "https://flagsapi.com/HT/shiny/64.png"
            ), CountryPhone(
                "Honduras",
                "HND",
                "+504",
                "https://flagsapi.com/HN/shiny/64.png"
            ), CountryPhone(
                "Hong kong",
                "HKG",
                "+852",
                "https://flagsapi.com/HK/shiny/64.png"
            ), CountryPhone(
                "Hungría",
                "HUN",
                "+36",
                "https://flagsapi.com/HU/shiny/64.png"
            ), CountryPhone(
                "India",
                "IND",
                "+91",
                "https://flagsapi.com/IN/shiny/64.png"
            ), CountryPhone(
                "Indonesia",
                "IDN",
                "+62",
                "https://flagsapi.com/ID/shiny/64.png"
            ), CountryPhone(
                "Irán",
                "IRN",
                "+98",
                "https://flagsapi.com/IR/shiny/64.png"
            ), CountryPhone(
                "Irak",
                "IRQ",
                "+964",
                "https://flagsapi.com/IQ/shiny/64.png"
            ), CountryPhone(
                "Irlanda",
                "IRL",
                "+353",
                "https://flagsapi.com/IE/shiny/64.png"
            ), CountryPhone(
                "Isla Bouvet",
                "BVT",
                "+47",
                "https://flagsapi.com/AQ/shiny/64.png"
            ), CountryPhone(
                "Isla de Man",
                "IMN",
                "+44",
                "https://flagsapi.com/IM/shiny/64.png"
            ), CountryPhone(
                "Isla de Navidad",
                "CXR",
                "+61",
                "https://flagsapi.com/CX/shiny/64.png"
            ), CountryPhone(
                "Isla Norfolk",
                "NFK",
                "+672",
                "https://flagsapi.com/NF/shiny/64.png"
            ), CountryPhone(
                "Islandia",
                "ISL",
                "+354",
                "https://flagsapi.com/IS/shiny/64.png"
            ), CountryPhone(
                "Islas Bermudas",
                "BMU",
                "+1441",
                "https://flagsapi.com/BM/shiny/64.png"
            ), CountryPhone(
                "Islas Caimán",
                "CYM",
                "+1345",
                "https://flagsapi.com/KY/shiny/64.png"
            ), CountryPhone(
                "Islas Cocos (Keeling)",
                "CCK",
                "+61",
                "https://flagsapi.com/CC/shiny/64.png"
            ), CountryPhone(
                "Islas Cook",
                "COK",
                "+682",
                "https://flagsapi.com/CK/shiny/64.png"
            ), CountryPhone(
                "Islas de Åland",
                "ALA",
                "+385",
                "https://flagsapi.com/AX/shiny/64.png"
            ), CountryPhone(
                "Islas Feroe",
                "FRO",
                "+298",
                "https://flagsapi.com/FO/shiny/64.png"
            ), CountryPhone(
                "Islas Georgias del Sur y Sandwich del Sur",
                "SGS",
                "+500",
                "https://flagsapi.com/GS/shiny/64.png"
            ), CountryPhone(
                "Islas Maldivas",
                "MDV",
                "+960",
                "https://flagsapi.com/MV/shiny/64.png"
            ), CountryPhone(
                "Islas Malvinas",
                "FLK",
                "+500",
                "https://flagsapi.com/FK/shiny/64.png"
            ), CountryPhone(
                "Islas Marianas del Norte",
                "MNP",
                "+1670",
                "https://flagsapi.com/MP/shiny/64.png"
            ), CountryPhone(
                "Islas Marshall",
                "MHL",
                "+692",
                "https://flagsapi.com/MH/shiny/64.png"
            ), CountryPhone(
                "Islas Pitcairn",
                "PCN",
                "+870",
                "https://flagsapi.com/PN/shiny/64.png"

            ), CountryPhone(
                "Islas Salomón",
                "SLB",
                "+677",
                "https://flagsapi.com/SB/shiny/64.png"

            ), CountryPhone(
                "Islas Turcas y Caicos",
                "TCA",
                "+1649",
                "https://flagsapi.com/TC/shiny/64.png"
            ), CountryPhone(
                "Islas Ultramarinas Menores de Estados Unidos",
                "UMI",
                "+246",
                "https://flagsapi.com/VI/shiny/64.png"
            ), CountryPhone(
                "Islas Vírgenes Británicas",
                "VGB",
                "+1248",
                "https://flagsapi.com/VG/shiny/64.png"
            ), CountryPhone(
                "Islas Vírgenes de los Estados Unidos",
                "VIR",
                "+1340",
                "https://flagsapi.com/US/shiny/64.png"
            ), CountryPhone(
                "Israel",
                "ISR",
                "+972",
                "https://flagsapi.com/IL/shiny/64.png",
            ), CountryPhone(
                "Italia",
                "ITA",
                "+39",
                "https://flagsapi.com/IT/shiny/64.png",
            ), CountryPhone(
                "Jamaica",
                "JAM",
                "+1876",
                "https://flagsapi.com/JM/shiny/64.png",

                ), CountryPhone(
                "Japón",
                "JPN",
                "+81",
                "https://flagsapi.com/JP/shiny/64.png",
            ), CountryPhone(
                "Jersey",
                "JEY",
                "+44",
                "https://flagsapi.com/JE/shiny/64.png",
            ), CountryPhone(
                "Jordania",
                "JOR",
                "+962",
                "https://flagsapi.com/JO/shiny/64.png",
            ), CountryPhone(
                "Kazajistán",
                "KAZ",
                "+7",
                "https://flagsapi.com/KZ/shiny/64.png",
            ), CountryPhone(
                "Kenia",
                "KEN",
                "+254",
                "https://flagsapi.com/KE/shiny/64.png",
            ), CountryPhone(
                "Kirguistán",
                "KGZ",
                "+996",
                "https://flagsapi.com/KG/shiny/64.png",
            ), CountryPhone(
                "Kiribati",
                "KIR",
                "+686",
                "https://flagsapi.com/KI/shiny/64.png",
            ), CountryPhone(
                "Kuwait",
                "KWT",
                "+965",
                "https://flagsapi.com/KW/shiny/64.png"
            ), CountryPhone(
                "Líbano",
                "LBN",
                "+961",
                "https://flagsapi.com/LB/shiny/64.png",
            ), CountryPhone(
                "Laos",
                "LAO",
                "+856",
                "https://flagsapi.com/LA/shiny/64.png",
            ), CountryPhone(
                "Lesoto",
                "LSO",
                "+266",
                "https://flagsapi.com/LS/shiny/64.png",
            ), CountryPhone(
                "Letonia",
                "LVA",
                "+371",
                "https://flagsapi.com/LV/shiny/64.png",
            ), CountryPhone(
                "Liberia",
                "LBR",
                "+231",
                "https://flagsapi.com/LR/shiny/64.png",
            ), CountryPhone(
                "Libia",
                "LBY",
                "+218",
                "https://flagsapi.com/LY/shiny/64.png",
            ), CountryPhone(
                "Liechtenstein",
                "LIE",
                "+423",
                "https://flagsapi.com/LI/shiny/64.png",
            ), CountryPhone(
                "Lituania",
                "LTU",
                "+370",
                "https://flagsapi.com/LT/shiny/64.png",
            ), CountryPhone(
                "Luxemburgo",
                "LUX",
                "+352",
                "https://flagsapi.com/LU/shiny/64.png",
            ),  CountryPhone(
                "Mónaco",
                "MCO",
                "+377",
                "https://flagsapi.com/MC/shiny/64.png",
            ), CountryPhone(
                "Macao",
                "MAC",
                "+853",
                "https://flagsapi.com/MO/shiny/64.png"
            ), CountryPhone(
                "Macedônia",
                "MKD",
                "+389",
                "https://flagsapi.com/MK/shiny/64.png",
            ), CountryPhone(
                "Madagascar",
                "MDG",
                "+261",
                "https://flagsapi.com/MG/shiny/64.png",
            ), CountryPhone(
                "Malasia",
                "MYS",
                "+60",
                "https://flagsapi.com/MY/shiny/64.png",
            ), CountryPhone(
                "Malawi",
                "MWI",
                "+265",
                "https://flagsapi.com/MW/shiny/64.png",
            ), CountryPhone(
                "Mali",
                "MLI",
                "+223",
                "https://flagsapi.com/ML/shiny/64.png",
            ), CountryPhone(
                "Malta",
                "MLT",
                "+356",
                "https://flagsapi.com/MT/shiny/64.png",
            ), CountryPhone(
                "Marruecos",
                "MAR",
                "+212",
                "https://flagsapi.com/MA/shiny/64.png",
            ), CountryPhone(
                "Martinica",
                "MTQ",
                "+596",
                "https://flagsapi.com/MQ/shiny/64.png",
            ), CountryPhone(
                "Mauricio",
                "MUS",
                "+230",
                "https://flagsapi.com/MU/shiny/64.png",
            ), CountryPhone(
                "Mauritania",
                "MRT",
                "+222",
                "https://flagsapi.com/MR/shiny/64.png",
            ), CountryPhone(
                "Mayotte",
                "MYT",
                "+262",
                "https://flagsapi.com/YT/shiny/64.png",
            ), CountryPhone(
                "Micronesia",
                "FSM",
                "+691",
                "https://flagsapi.com/FM/shiny/64.png",
            ), CountryPhone(
                "Moldavia",
                "MDA",
                "+373",
                "https://flagsapi.com/MD/shiny/64.png",
            ), CountryPhone(
                "Mongolia",
                "MNG",
                "+976",
                "https://flagsapi.com/MN/shiny/64.png",
            ), CountryPhone(
                "Montenegro",
                "MNE",
                "+382",
                "https://flagsapi.com/ME/shiny/64.png",
            ), CountryPhone(
                "Montserrat",
                "MSR",
                "+1644",
                "https://flagsapi.com/MS/shiny/64.png",
            ), CountryPhone(
                "Mozambique",
                "MOZ",
                "+258",
                "https://flagsapi.com/MZ/shiny/64.png",
            ), CountryPhone(
                "Namibia",
                "NAM",
                "+264",
                "https://flagsapi.com/NA/shiny/64.png",
            ), CountryPhone(
                "Nauru",
                "NRU",
                "+674",
                "https://flagsapi.com/NR/shiny/64.png",
            ), CountryPhone(
                "Nepal",
                "NPL",
                "+977",
                "https://flagsapi.com/NP/shiny/64.png",
            ), CountryPhone(
                "Nicaragua",
                "NIC",
                "+505",
                "https://flagsapi.com/NI/shiny/64.png",
            ), CountryPhone(
                "Niger",
                "NER",
                "+227",
                "https://flagsapi.com/NE/shiny/64.png",
            ), CountryPhone(
                "Nigeria",
                "NGA",
                "+234",
                "https://flagsapi.com/NG/shiny/64.png",
            ), CountryPhone(
                "Niue",
                "NIU",
                "+683",
                "https://flagsapi.com/NU/shiny/64.png",
            ), CountryPhone(
                "Noruega",
                "NOR",
                "+47",
                "https://flagsapi.com/NO/shiny/64.png",
            ), CountryPhone(
                "Nueva Caledonia",
                "NCL",
                "+687",
                "https://flagsapi.com/NC/shiny/64.png",
            ), CountryPhone(
                "Nueva Zelanda",
                "NZL",
                "+64",
                "https://flagsapi.com/NZ/shiny/64.png",
            ), CountryPhone(
                "Omán",
                "OMN",
                "+968",
                "https://flagsapi.com/OM/shiny/64.png",
            ), CountryPhone(
                "Países Bajos",
                "NLD",
                "+31",
                "https://flagsapi.com/NL/shiny/64.png",
            ), CountryPhone(
                "Pakistán",
                "PAK",
                "+92",
                "https://flagsapi.com/PK/shiny/64.png",
            ), CountryPhone(
                "Palau",
                "PLW",
                "+680",
                "https://flagsapi.com/PW/shiny/64.png",
            ), CountryPhone(
                "Palestina",
                "PSE",
                "+970",
                "https://flagsapi.com/PS/shiny/64.png",
            ), CountryPhone(
                "Panamá",
                "PAN",
                "+507",
                "https://flagsapi.com/PA/shiny/64.png",
            ), CountryPhone(
                "Papúa Nueva Guinea",
                "PNG",
                "+675",
                "https://flagsapi.com/PG/shiny/64.png",
            ), CountryPhone(
                "Paraguay",
                "PRY",
                "+595",
                "https://flagsapi.com/PY/shiny/64.png",
            ), CountryPhone(
                "Perú",
                "PER",
                "+51",
                "https://flagsapi.com/PE/shiny/64.png",
            ), CountryPhone(
                "Polinesia Francesa",
                "PYF",
                "+689",
                "https://flagsapi.com/PF/shiny/64.png",
            ), CountryPhone(
                "Polonia",
                "POL",
                "+48",
                "https://flagsapi.com/PL/shiny/64.png"
            ), CountryPhone(
                "Portugal",
                "PRT",
                "+351",
                "https://flagsapi.com/PT/shiny/64.png",
            ), CountryPhone(
                "Puerto Rico",
                "PRI",
                "+1",
                "https://flagsapi.com/PR/shiny/64.png",
            ), CountryPhone(
                "Qatar",
                "QAT",
                "+974",
                "https://flagsapi.com/QA/shiny/64.png",
            ), CountryPhone(
                "Reino Unido",
                "GBR",
                "+44",
                "https://flagsapi.com/GB/shiny/64.png",
            ), CountryPhone(
                "República Centroafricana",
                "CAF",
                "+236",
                "https://flagsapi.com/CF/shiny/64.png",
            ), CountryPhone(
                "República Checa",
                "CZE",
                "+420",
                "https://flagsapi.com/CZ/shiny/64.png",
            ), CountryPhone(
                "República Dominicana",
                "DOM",
                "+1809",
                "https://flagsapi.com/DO/shiny/64.png",
            ), CountryPhone(
                "República de Sudán del Sur",
                "SSD",
                "+211",
                "https://flagsapi.com/SS/shiny/64.png",
            ), CountryPhone(
                "Reunión",
                "REU",
                "+262",
                "https://flagsapi.com/RE/shiny/64.png",
            ), CountryPhone(
                "Ruanda",
                "RWA",
                "+250",
                "https://flagsapi.com/RW/shiny/64.png",
            ), CountryPhone(
                "Rumanía",
                "ROU",
                "+40",
                "https://flagsapi.com/RO/shiny/64.png",
            ), CountryPhone(
                "Rusia",
                "RUS",
                "+7",
                "https://flagsapi.com/RU/shiny/64.png",
            ), CountryPhone(
                "Sahara Occidental",
                "ESH",
                "+212",
                "https://flagsapi.com/EH/shiny/64.png",
            ), CountryPhone(
                "Samoa",
                "WSM",
                "+685",
                "https://flagsapi.com/WS/shiny/64.png",
            ), CountryPhone(
                "Samoa Americana",
                "ASM",
                "+1684",
                "https://flagsapi.com/AS/shiny/64.png",
            ), CountryPhone(
                "San Bartolomé",
                "BLM",
                "+590",
                "https://flagsapi.com/BM/shiny/64.png",
            ), CountryPhone(
                "San Cristóbal y Nieves",
                "KNA",
                "+1869",
                "https://flagsapi.com/KN/shiny/64.png",
            ), CountryPhone(
                "San Marino",
                "SMR",
                "+378",
                "https://flagsapi.com/SM/shiny/64.png",
            ), CountryPhone(
                "San Martín (Francia)",
                "MAF",
                "+1599",
                "https://flagsapi.com/MF/shiny/64.png",
            ), CountryPhone(
                "San Pedro y Miquelón",
                "SPM",
                "+508",
                "https://flagsapi.com/FR/shiny/64.png",
            ), CountryPhone(
                "San Vicente y las Granadinas",
                "VCT",
                "+1784",
                "https://flagsapi.com/VC/shiny/64.png",
            ), CountryPhone(
                "Santa Elena",
                "SHN",
                "+290",
                "https://flagsapi.com/SH/shiny/64.png",
            ), CountryPhone(
                "Santa Lucía",
                "LCA",
                "+1758",
                "https://flagsapi.com/LC/shiny/64.png",
            ), CountryPhone(
                "Santo Tomé y Príncipe",
                "STP",
                "+239",
                "https://flagsapi.com/ST/shiny/64.png",
            ), CountryPhone(
                "Senegal",
                "SEN",
                "+221",
                "https://flagsapi.com/SN/shiny/64.png",
            ), CountryPhone(
                "Serbia",
                "SRB",
                "+381",
                "https://flagsapi.com/RS/shiny/64.png",
            ), CountryPhone(
                "Seychelles",
                "SYC",
                "+248",
                "https://flagsapi.com/SC/shiny/64.png",
            ), CountryPhone(
                "Sierra Leona",
                "SLE",
                "+232",
                "https://flagsapi.com/SL/shiny/64.png",
            ), CountryPhone(
                "Singapur",
                "SGP",
                "+65",
                "https://flagsapi.com/SG/shiny/64.png",
            ), CountryPhone(
                "Siria",
                "SYR",
                "+963",
                "https://flagsapi.com/SY/shiny/64.png",
            ), CountryPhone(
                "Somalia",
                "SOM",
                "+252",
                "https://flagsapi.com/SO/shiny/64.png",
            ), CountryPhone(
                "Sri lanka",
                "LKA",
                "+94",
                "https://flagsapi.com/LK/shiny/64.png",
            ), CountryPhone(
                "Sudáfrica",
                "ZAF",
                "+27",
                "https://flagsapi.com/ZA/shiny/64.png",
            ), CountryPhone(
                "Sudán",
                "SDN",
                "+249",
                "https://flagsapi.com/SS/shiny/64.png",
            ), CountryPhone(
                "Suecia",
                "SWE",
                "+46",
                "https://flagsapi.com/SE/shiny/64.png",
            ), CountryPhone(
                "Suiza",
                "CHE",
                "+41",
                "https://flagsapi.com/CH/shiny/64.png",
            ), CountryPhone(
                "Surinám",
                "SUR",
                "+597",
                "https://flagsapi.com/SR/shiny/64.png",
            ), CountryPhone(
                "Swazilandia",
                "SWZ",
                "+268",
                "https://flagsapi.com/SZ/shiny/64.png",
            ), CountryPhone(
                "Tayikistán",
                "TJK",
                "+992",
                "https://flagsapi.com/TJ/shiny/64.png",
            ), CountryPhone(
                "Tailandia",
                "THA",
                "+66",
                "https://flagsapi.com/TH/shiny/64.png",
            ), CountryPhone(
                "Taiwán",
                "TWN",
                "+886",
                "https://flagsapi.com/TW/shiny/64.png",
            ), CountryPhone(
                "Tanzania",
                "TZA",
                "+255",
                "https://flagsapi.com/TZ/shiny/64.png",
            ), CountryPhone(
                "Togo",
                "TGO",
                "+228",
                "https://flagsapi.com/TG/shiny/64.png"
            ), CountryPhone(
                "Tokelau",
                "TKL",
                "+690",
                "https://flagsapi.com/TK/shiny/64.png"
            ), CountryPhone(
                "Tonga",
                "TON",
                "+676",
                "https://flagsapi.com/TO/shiny/64.png"
            ), CountryPhone(
                "Trinidad y Tobago",
                "TTO",
                "+1868",
                "https://flagsapi.com/TT/shiny/64.png"
            ), CountryPhone(
                "Tunez",
                "TUN",
                "+216",
                "https://flagsapi.com/TN/shiny/64.png"
            ), CountryPhone(
                "Turkmenistán",
                "TKM",
                "+993",
                "https://flagsapi.com/TM/shiny/64.png"
            ), CountryPhone(
                "Turquía",
                "TUR",
                "+90",
                "https://flagsapi.com/TR/shiny/64.png"
            ), CountryPhone(
                "Tuvalu",
                "TUV",
                "+688",
                "https://flagsapi.com/TV/shiny/64.png"
            ), CountryPhone(
                "Ucrania",
                "UKR",
                "+380",
                "https://flagsapi.com/UA/shiny/64.png"
            ), CountryPhone(
                "Uganda",
                "UGA",
                "+256",
                "https://flagsapi.com/UG/shiny/64.png"
            ), CountryPhone(
                "Uruguay",
                "URY",
                "+598",
                "https://flagsapi.com/UY/shiny/64.png"
            ), CountryPhone(
                "Uzbekistán",
                "UZB",
                "+998",
                "https://flagsapi.com/UZ/shiny/64.png"
            ), CountryPhone(
                "Vanuatu",
                "VUT",
                "+678",
                "https://flagsapi.com/VU/shiny/64.png"
            ), CountryPhone(
                "Venezuela",
                "VEN",
                "+58",
                "https://flagsapi.com/VE/shiny/64.png"
            ), CountryPhone(
                "Vietnam",
                "VNM",
                "+84",
                "https://flagsapi.com/VN/shiny/64.png"
            ), CountryPhone(
                "Wallis y Futuna",
                "WLF",
                "+681",
                "https://flagsapi.com/WF/shiny/64.png"
            ), CountryPhone(
                "Yemen",
                "YEM",
                "+967",
                "https://flagsapi.com/YE/shiny/64.png"
            ), CountryPhone(
                "Yibuti",
                "DJI",
                "+253",
                "https://flagsapi.com/DJ/shiny/64.png"
            ), CountryPhone(
                "Zambia",
                "ZMB",
                "+260",
                "https://flagsapi.com/ZM/shiny/64.png"
            ), CountryPhone(
                "Zimbabue",
                "ZWE",
                "+263",
                "https://flagsapi.com/ZW/shiny/64.png"
            )
        )
    }
}
