package com.venturessoft.human.views.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivityAnimotionLottieBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.Preferences

private lateinit var binding: ActivityAnimotionLottieBinding
class AnimotionLottie : AppCompatActivity() {
    var user: User? = null

    companion object {
        var redirect: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimotionLottieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = Preferences.getUser(this.applicationContext)
        loadAnimationData()
    }

    private fun loadAnimationData() {
        val mapa: String? = intent.getStringExtra("Estacion")
        val email: String? = intent.getStringExtra("email")
        val redirectView: String? = intent.getStringExtra("Redirect")


        if (mapa == "Mapa") {
            binding.lottieSuccess.setAnimation("lottieanimation/map-location.json")
            // lottie_success.setAnimation("location.json")
            binding.lottieSuccess.playAnimation()
            binding.lottieSuccess.loop(true)
            redirect = "Mapa"

            Handler().postDelayed({
                finish()
            }, Constants.ANIMOTIONLOTTIE_TIMEOUT)
        } else {
            //
            if (intent.getStringExtra("Redirect") != null) {
                redirect = intent.getStringExtra("Redirect")!!
            }

            val getEmail: String? = intent.getStringExtra("email")



            if (getEmail != null) {
                binding.lottieSuccess.setAnimation("lottieanimation/successLottie.json")
                binding.lottieSuccess.playAnimation()
                binding.lottieSuccess.loop(true)
                Toast.makeText(this,getString(R.string.toast_company_success),Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    finishAffinity()
                    val intent = Intent(this@AnimotionLottie, LoginActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }, Constants.ANIMOTIONLOTTIE_TIMEOUT)

            }
        }

        when (redirectView) {
            "ListZone" -> {
                binding.lottieSuccess.setAnimation("lottieanimation/successLottie.json")
                binding.lottieSuccess.playAnimation()
                binding.lottieSuccess.loop(true)
                Toast.makeText(this,getString(R.string.toast_zonas_success),Toast.LENGTH_SHORT).show()

                redirect = "ListZones"
                Handler().postDelayed({
                    finish()
                }, Constants.ANIMOTIONLOTTIE_TIMEOUT)

            }
            "ListSupervisor" -> {
                binding.lottieSuccess.setAnimation("lottieanimation/successLottie.json")
                binding.lottieSuccess.playAnimation()
                binding.lottieSuccess.loop(true)
                redirect = "ListSupervisor"

                Toast.makeText(this,getString(R.string.toast_supervisor_success),Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    finish()
                }, Constants.ANIMOTIONLOTTIE_TIMEOUT)

            }
            "ListEmployee" -> {
                binding.lottieSuccess.setAnimation("lottieanimation/successLottie.json")
                binding.lottieSuccess.playAnimation()
                binding.lottieSuccess.loop(true)
                Toast.makeText(this,getString(R.string.toast_employee_success),Toast.LENGTH_SHORT).show()

                redirect = "ListEmployee"
                Handler().postDelayed({
                    finish()
                }, Constants.ANIMOTIONLOTTIE_TIMEOUT)
            }
            "PhotoAut" -> {
                binding.lottieSuccess.setAnimation("lottieanimation/successLottie.json")
                binding.lottieSuccess.playAnimation()
                binding.lottieSuccess.loop(true)

                redirect = "ListEmployee"
                Handler().postDelayed({
                    finish()
                }, Constants.ANIMOTIONLOTTIE_TIMEOUT)
            }
            "ListStation" -> {
                binding.lottieSuccess.setAnimation("lottieanimation/successLottie.json")
                binding.lottieSuccess.playAnimation()
                binding.lottieSuccess.loop(true)
                Toast.makeText(this,getString(R.string.toast_station_success),Toast.LENGTH_SHORT).show()

                redirect = "ListStation"
                Handler().postDelayed({
                    finish()
                }, Constants.ANIMOTIONLOTTIE_TIMEOUT)
            }

            "loadLanguage" -> {
                binding.lottieSuccess.setAnimation("lottieanimation/configuracion.json")
                binding.lottieSuccess.playAnimation()
                binding.lottieSuccess.loop(true)
                Toast.makeText(this,getString(R.string.toast_configuracion_success),Toast.LENGTH_SHORT).show()

                redirect = ""
                Handler().postDelayed({
                    val intent = Intent(this, NavigationDrawerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)

                }, Constants.ANIMOTIONLOTTIE_TIMEOUT)
            }
            "EmailAnimation"->{
                binding.lottieSuccess.setAnimation("lottieanimation/zemail.json")
                binding.lottieSuccess.playAnimation()
                binding.lottieSuccess.loop(true)
                redirect = "ListSupervisor"
              Toast.makeText(this,getString(R.string.toast_report_email_success),Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    finish()
                }, Constants.EMAIL_ANIMOTIONLOTTIE_TIMEOUT)
            }
        }
    }
}

