package com.example.sumit.echo.activities

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.example.sumit.echo.R
import com.example.sumit.echo.adapters.NavigationDrawerAdapter
import com.example.sumit.echo.fragments.MainScreenFragment
import com.example.sumit.echo.fragments.SongPlayingFragment

class MainActivity : AppCompatActivity() {

    var contentList_for_navdrawer:ArrayList<String> = arrayListOf()
    var images_for_navdrawer = intArrayOf(R.drawable.navigation_allsongs,R.drawable.navigation_favorites,R.drawable.navigation_settings,R.drawable.navigation_aboutus)
    var trackNotificationBuilder:Notification?=null
    object Static{
        var drawerLayout : DrawerLayout? = null
        var notificationManager : NotificationManager?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar =findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        contentList_for_navdrawer.add("All Songs")
        contentList_for_navdrawer.add("Favorites")
        contentList_for_navdrawer.add("Settings")
        contentList_for_navdrawer.add("About Us")
        MainActivity.Static.drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this@MainActivity,MainActivity.Static.drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        MainActivity.Static.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        val mainScreenFragment = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment,mainScreenFragment,"MainScreenFragment")
                .commit()

        var navigationAdapter = NavigationDrawerAdapter(contentList_for_navdrawer,images_for_navdrawer,this)
        navigationAdapter.notifyDataSetChanged()
        var navigation_recycler_view = findViewById<RecyclerView>(R.id.navigation_recycler_view)
        navigation_recycler_view.layoutManager = LinearLayoutManager(this)
        //Check later what item animator does by disabling it
        navigation_recycler_view.itemAnimator = DefaultItemAnimator()
        navigation_recycler_view.adapter = navigationAdapter
        navigation_recycler_view.setHasFixedSize(true)

        val intent = Intent(this,MainActivity::class.java)
        val pIntent=PendingIntent.getActivity(this,System.currentTimeMillis().toInt(),intent,0)
        trackNotificationBuilder=Notification.Builder(this)
                .setContentTitle("A track is playing in background")
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.echo_logo)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()
        Static.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStart() {
        super.onStart()
        try {
            Static.notificationManager?.cancel(1978)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Static.notificationManager?.cancel(1978)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    override fun onStop() {
        super.onStop()
        try {
            if(SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                Static.notificationManager?.notify(1978,trackNotificationBuilder)
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}
