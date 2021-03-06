# Fyber_challenge_android

Fyber_challenge_andorid has been updated to 2.5 . 
This is the reference of Android application to make some SNS or O2O service on Android. New drawer and awesome funtionalaties were applied into Fyber App. All Android App developer can see how to make some SNS or O2O service on Android via the Fyber source code. The Fyber App currently works well now. 
All technical things/stuffs to make SNS or 020 service on Android are provided into this open-source project.
If you want to make the food delivery service, shopping or O2O service App on Android, please refer to my open-source project and you can make awesome those App very easily. If you want to learn more technical knowledge of Web-Communication, which is related Web APIs, please visit my [BEatery](https://github.com/Lukoh/beateries) open-source project and [BEatery APIs](https://github.com/Lukoh/beateries/blob/master/BEatery%20REST%20APIs.pdf).  You can get more details you need. Also you can know the way how to communicate with Server. if you want to get this technical knowledge, please visit here below:

[Fyber Web Communicator](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/fyber_challenge/web/communicator/RequestClient.java) 

[BEeatery Web Communicator](https://github.com/Lukoh/beateries/tree/master/app/src/main/java/com/goforer/beatery/web)

Let me know if you want to add some menu list into SlidingDrawer. I can help you to make some menu list, you need, in SlidingDrawer, which is based on [MaterialDrawer](https://github.com/mikepenz/MaterialDrawer). It's easy to make some specific menu list. Please visit below link if you want to get more:

[SlidingDrawer](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/fyber_challenge/ui/view/drawer/SlidingDrawer.java)

RecyclerFragment was improved to be more efficient and more great. Please refer to [RecyclerFragment.java](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/base/ui/fragment/RecyclerFragment.java).

[A window into transitions](https://github.com/Lukoh/Fyber_challenge_android/tree/master/app/src/main/java/com/goforer/fyber_challenge/ui/transition)

Motion helps guide your users through your app by providing hierarchy and spacial relationships. Transitions are the best way to implement this kind of scene change and to choreograph the journey through your app. This provides a deep dive on Android's transition APIs focusing on content and shared element transitions.

Here is [demo video](https://youtu.be/9uxE3ei-Wwo). Please watch this demo video if you'd like to know how Transition effect is runs.

###A quick overview what's in
- **the easiest structure**
- quick and simple code
- **applied Retrofit, EventBus and ButterKnife library**
- applied Google Material Design
- **uses the AppCompat support library**
- comes with multiple Activities and Fragments
- based on a **RecyclerView**
- **RTL** support
- expandable fragments

#New Feature
[Version 2.5] Transitions are the best way to implement this kind of scene change and to choreograph the journey through your app. Version 2.5 provides a deep dive on Android's transition APIs focusing on content and shared element transitions.

[A window into transitions](https://github.com/Lukoh/Fyber_challenge_android/tree/master/app/src/main/java/com/goforer/fyber_challenge/ui/transition)

Motion helps guide your users through your app by providing hierarchy and spacial relationships. Transitions are the best way to implement this kind of scene change and to choreograph the journey through your app. This provides a deep dive on Android's transition APIs focusing on content and shared element transitions.

Here is [demo video](https://youtu.be/9uxE3ei-Wwo). Please watch this demo video if you'd like to know how Transition effect is runs.

[Version 2.4] Sort functions(by Title, Payout, Bookmart amount and subscription amount) hass been applied into FyberChallenge.

[Version 2.3.8] Offer Information sharing function has been applied into FyberChallenge. 

(User can share that to Facebook or other SNS, such as Google+, WhatsApp, gmail, and so on.)

Please refer to below file if you'd like to see more about sharing Offer Information to Facebook or ohter SNS:

[OffersInfoActivity](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/base/ui/activity/OffersInfoActivity.java)

Also, if you'd like to see how to apply Facebook SDK to FyberChallenge, please visit to [Facebook SDK](https://developers.facebook.com/docs/sharing/android).

The performance of SlidingDrawer has been improved very efficiently. That is, Offer slides, transitions between one entire screen to another,  result in higher performance. 
The new functions for deleting an item on the list(Offer) has been applied.
It's run by the swipe action. Please return null in [createItemTouchHelperToRecyclerView](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/base/ui/fragment/RecyclerFragment.java) method if a user don't want to attach the function of removing an item to the fragment which is derived from [RecyclerFragment](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/base/ui/fragment/RecyclerFragment.java) like below example:

@Override

protected ItemTouchHelper.Callback createItemTouchHelper() {

    return null;

}

or would like to attach that like below example:

@Override

protected ItemTouchHelper.Callback createItemTouchHelper() {
    
    return new RecyclerItemTouchHelperCallback(mContext, mAdapter, Color.RED);
    
}


Please refer to below files if you'd like to see more about deleting an item on the list(Offer):

[RecyclerFragment](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/base/ui/fragment/RecyclerFragment.java)

[ItemTouchHelperListener](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/base/ui/helper/ItemTouchHelperListener.java)

[RecyclerItemTouchHelperCallback](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/base/ui/helper/RecyclerItemTouchHelperCallback.java)


The sliding function has been applied into seeing Offer-Inforamtion module.

A new function is applied into Fyber Challenge App. [OfferInfoActivity](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/fyber_challenge_android/ui/activity/OffersInfoActivity.java) consists of two parts. The top area in [OfferInfoActivity](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/fyber_challenge_android/ui/activity/OffersInfoActivity.java) is the AppBarLayout, and the bottom area is the FrameLayout which is wrapped by the [SwipeViewPager](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/base/ui/view/SwipeViewPager.java). So I made it could works like a part not two distinct parts. It's very efficient. I  just used two ImageView widgets to work/look like a ViewPager and it provides more awesome function.  
For more information, see the [OfferInfoActivity.java](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/fyber_challenge_android/ui/activity/OffersInfoActivity.java) and [activity_offers_info.xml](https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/res/layout/activity_offers_info.xml) file.

This technique can enable your App to have more powerful function and great UX. Please watch this demo video if you'd like to know how that function works or how to provide great UX. 
Here is [demo video](https://youtu.be/93VroFq11ws). 

#Preview
##Demo
You can try it out here [Google Play](https://play.google.com/store/apps/details?id=com.goforer.fyber_challenge) (an open source application). Or you can download the [Fyber Challenge Application](https://play.google.com/store/apps/details?id=com.goforer.fyber_challenge)

Here is [demo video](https://youtu.be/93VroFq11ws). Please watch this demo video if you'd like to know how Fyber App runs. 

Here is [demo video](https://youtu.be/9uxE3ei-Wwo). Please watch this demo video if you'd like to know how Transition effect is runs.

##Screenshots
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164240.png" alt="Log-in Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164300.png" alt="Log-in Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164335.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164502.png" alt="Screen Demo" width="350" /> 
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164515.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164556.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164603.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164624.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164631.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164708.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164712.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164730.png" alt="Screen Demo" width="350" />
&nbsp;
<img src="https://github.com/Lukoh/Fyber_challenge_android/blob/master/Screenshot_20160710-164744.png" alt="Screen Demo" width="350" />

## Libraries

This app leverages third-party libraries:

 * [Retrofit](http://square.github.io/retrofit/) - For asynchronous network requests
 * [EventBus](http://greenrobot.org/eventbus/) - For communication between Activiteis, Fragments, Servcie, etc
 * [ButterKnife](http://jakewharton.github.io/butterknife/) - For field and method binding for Android views
 * [Glide](https://github.com/bumptech/glide) - For an image loading and caching library for Android focused on smooth scrolling
 * [SwipyRefreshLayout](https://github.com/OrangeGangsters/SwipyRefreshLayout) - For swiping in both direction
 * [FloatingActionButton](https://github.com/Clans/FloatingActionButton) - For using floating action button
 * [MaterialDrawer](https://github.com/mikepenz/MaterialDrawer) - For using drawer menu
  - I added/implmented more goood containers to be input into MaterialDrawer. Please refer to [SlidingDrawer]( https://github.com/Lukoh/Fyber_challenge_android/blob/master/app/src/main/java/com/goforer/fyber_challenge/ui/view/drawer/SlidingDrawer.java) 

# License
```
Copyright 2015-2016 Lukoh Nam, goForer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
