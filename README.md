_*This project is not in active development or support*_

<img alt="pnr status app" src="https://raw.githubusercontent.com/midhunhk/pnr-status-app/master/resources/v4/promotional/fg-notan-02.png"/> 

[![CircleCI](https://img.shields.io/circleci/project/github/midhunhk/pnr-status-app/master.svg?label=CircleCI)](https://circleci.com/gh/midhunhk/pnr-status-app/tree/master) 
[![Release](https://img.shields.io/github/release/midhunhk/pnr-status-app.svg)](https://github.com/midhunhk/pnr-status-app/releases) 
[![GitHub commits](https://img.shields.io/github/commits-since/midhunhk/pnr-status-app/v4.1.0.svg)](https://github.com/midhunhk/pnr-status-app) 
[![Issues](https://img.shields.io/github/issues/midhunhk/pnr-status-app.svg)](https://github.com/midhunhk/pnr-status-app/issues) 

[Project Wiki](https://github.com/midhunhk/pnr-status-app/wiki) | 
[Github Page](http://midhunhk.github.io/pnr-status-app) |
[Versions](https://github.com/midhunhk/pnr-status-app/wiki/Versions)  
  
This is an Android App that can check the PNR Status of Train tickets in India. Since this is not an Official app, nor there is an official API available for checking the same, we need to rely on unreliable third party services and crawl the responses

**Note:** This project wasn't in active development or supported since 2013, but is getting updated to target the latest Android API as well as to remove the `READ_SMS` permission which is required by Google Play from March 2019. There is no guarantee for adding additional services or fixing any issues :)

### Services Implemented
The list of API Services and their status is listed [here](https://github.com/midhunhk/pnr-status-app/wiki/Services)

### Screenshots
<img alt="PNR Status App" src="https://github.com/midhunhk/pnr-status-app/blob/notan/resources/v4/promotional/01-landing.png" width="250"/> <img alt="PNR Status App" src="https://github.com/midhunhk/pnr-status-app/blob/notan/resources/v4/promotional/02-ticket-details.png" width="250"/>

## Installation
Clone this repo and import the project as an existing Android project in Android Studio.
```
git clone https://github.com/midhunhk/pnr-status-app.git  

Open the project in Android Studio  
```

**Note**: This project is an unofficial app and not related or endorsed by Indian Rail.

### Play Store
<a href="https://play.google.com/store/apps/details?id=com.ae.apps.pnrstatus.v3">
 <img alt="Get it on Google Play" width="200px" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png">
</a>

### Beta Channel
Opt-in to the Beta channel by visiting the link: https://play.google.com/apps/testing/com.ae.apps.messagecounter 

### License
`
MIT License

Copyright (c) 2024 Midhun Harikumar

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
`

### References
First versions created as Flex desktop app [PNRStatusApp-Flex](https://github.com/midhunhk/pnr-status-app-flex)
