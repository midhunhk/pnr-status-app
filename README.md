## pnr-status-app
This is an Android App that can check the PNR Status of Train tickets in India. Since this is not an Official app, nor there is an official API available for checking the same, we need to rely on unreliable third party services and crawl the responses

**Note:** This project wasn't in active development or supported since 2013, but is getting updated to target the latest Android API as well as to remove the `READ_SMS` permission which is required by Google Play from March 2019. There is no guarantee for adding additional services or fixing any issues :)

### Services Implemented
The following third party services were implemented, but there is no guarantee of which one may work.

* ~~PNRAPI~~ by @alagu (https://github.com/alagu/pnrapi-ruby)
* ~~Indian Rail Service~~ (http://www.indianrail.gov.in/)
* ~~PNRStatus~~ (http://www.pnrstatus.in)
* IRCTC PNR Status (http://irctc-pnr-status.com)

The striken out services do not seem to be working at present.

### Major Milestones
- Version 3.0.0 [August 2012]
- Version 3.1.1 [January 2013]
- Version 3.2.25 [March 2014]
- Version 3.2.5  [May 2016]
- Version 4.0.0 [March 2019]

**Note**: This project is an unofficial app and not related or endorsed by Indian Rail.

### References
First version created as Flex desktop app [PNRStatusApp-Flex](https://github.com/midhunhk/pnr-status-app-flex)
