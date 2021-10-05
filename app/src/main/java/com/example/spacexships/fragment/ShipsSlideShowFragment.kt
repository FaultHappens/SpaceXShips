package com.example.spacexships.fragment


import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.spacexships.data.ShipsData
import com.example.spacexships.databinding.FragmentShipsSlideShowBinding
import com.example.spacexships.adapter.SlideShowAdapter
import com.example.spacexships.viewmodel.ShipsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList


class ShipsSlideShowFragment : Fragment() {
    private var plays: Boolean = true
    private var slideShowSpeed = 1
    private lateinit var geoCoder: Geocoder
    private var addressList = mutableListOf<Address>()
    lateinit var launches: Array<String>
    lateinit var location: LatLng
    private var swipeTimer: Timer = Timer()
    lateinit var ships: ArrayList<ShipsData>
    lateinit var fragmentShipsSlideShowBinding: FragmentShipsSlideShowBinding
    private lateinit var slideShowAdapter: SlideShowAdapter
    private lateinit var timerTask: TimerTask
    private var currentImage: Int = 0
    private val model: ShipsViewModel by viewModels()
    private lateinit var googleMap: GoogleMap
    private lateinit var navController: NavController
    private val animationDelay: Long = 500
    private val animationPeriod: Long = 5000



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentShipsSlideShowBinding = FragmentShipsSlideShowBinding.inflate(layoutInflater)
        val supportMapFragment = childFragmentManager.findFragmentById(com.example.spacexships.R.id.mapFragment) as SupportMapFragment
        supportMapFragment.getMapAsync{map-> googleMap = map }
        return fragmentShipsSlideShowBinding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        geoCoder = Geocoder(activity)
        model.getShips().observe(viewLifecycleOwner, { users ->
            ships = users
            slideShowAdapter = SlideShowAdapter(ships, context)
            fragmentShipsSlideShowBinding.viewPager.adapter = slideShowAdapter
            swipeTimer.schedule(addTimerTask(), animationDelay, animationPeriod / slideShowSpeed.toLong())
        })

        fragmentShipsSlideShowBinding.forwardButton.setOnClickListener{
            when(currentImage){
                ships.size-1 -> currentImage = 0
                else -> currentImage++
            }
            fragmentShipsSlideShowBinding.viewPager.setCurrentItem(currentImage, true)
            shipInfo()
        }

        fragmentShipsSlideShowBinding.backButton.setOnClickListener{
            when(currentImage){
                0 -> currentImage = ships.size-1
                else -> currentImage--
            }
            fragmentShipsSlideShowBinding.viewPager.setCurrentItem(currentImage, true)
            shipInfo()
        }

        fragmentShipsSlideShowBinding.replayButton.setOnClickListener {
            //Todo ანიმაცია შეგიძლია ცალკე მეთოდში გაიტანო
            val rotateAnimationFromDegrees = 360F
            val rotateAnimationToDegrees = 0F
            val rotateAnimationDuration: Long = 300
            val rotateAnimation = RotateAnimation(
                rotateAnimationFromDegrees, rotateAnimationToDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotateAnimation.interpolator = LinearInterpolator()
            rotateAnimation.duration = rotateAnimationDuration
            rotateAnimation.fillAfter = true
            it.startAnimation(rotateAnimation)
            currentImage = 0
            fragmentShipsSlideShowBinding.viewPager.setCurrentItem(currentImage, true)
            shipInfo()
        }

        fragmentShipsSlideShowBinding.speedButton.setOnClickListener {
            when (slideShowSpeed) {
                5 -> {
                    slideShowSpeed = 1
                }
                else -> {
                    slideShowSpeed++
                }
            }
            fragmentShipsSlideShowBinding.speedButton.text = "${slideShowSpeed}x"
            timerTask.cancel()
            swipeTimer.purge()
            swipeTimer.schedule(addTimerTask(), animationDelay, animationPeriod / slideShowSpeed.toLong())

        }

        fragmentShipsSlideShowBinding.slideShowToggleButton.setOnClickListener {
            if (plays) {
                timerTask.cancel()
            } else {
                swipeTimer.schedule(
                    addTimerTask(),
                    animationDelay,
                    animationPeriod / slideShowSpeed.toLong()
                )
            }
            plays = !plays
            it.clearAnimation()
            it.animate().rotationXBy(90F).withEndAction {
                it.isActivated = (!it.isActivated)
                it.animate().rotationXBy(90F).setDuration(150).start()
            }.duration = 150
        }
    }


    private fun addTimerTask(): TimerTask {
        timerTask = object : TimerTask() {

            override fun run() {
                activity?.runOnUiThread {
                    when(currentImage){
                        ships.size-1 -> currentImage=0
                        else -> currentImage++
                    }
                    fragmentShipsSlideShowBinding.viewPager.setCurrentItem(
                        currentImage,
                        true
                    )
                    shipInfo()
                }
            }
        }
        return timerTask

    }
    private fun shipInfo(){
        fragmentShipsSlideShowBinding.shipNameTV.text = ships[currentImage].name
        fragmentShipsSlideShowBinding.shipTypeTV.text = ships[currentImage].type
        fragmentShipsSlideShowBinding.shipHomePortTV.text =
            ships[currentImage].home_port
        fragmentShipsSlideShowBinding.shipNameTV.setOnClickListener {
            launches = ships[currentImage - 1].launches.toTypedArray()
            navController.navigate(ShipsSlideShowFragmentDirections.actionShipsSlideShowFragmentToLaunchesListFragment(
                launches
            ))
            timerTask.cancel()

        }
        addressList = geoCoder.getFromLocationName(ships[currentImage].home_port, 1)
        location = LatLng(addressList[0].latitude, addressList[0].longitude)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13.0f))
    }
}