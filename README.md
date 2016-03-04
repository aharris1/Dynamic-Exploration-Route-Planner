# Dynamic Exploration Route Planner (DERP)

Utilizes data provided by CREST to dynamically plan exploration routes optimised for profit and (relative) safety.

Steps for initial authorization and setup:
1. [Press EVE SSO Login](step1.png)
2. [Copy URL to the left](step2.png)
3. Login and select character
4. [Enter the code from the URL into the SSO Login Code field](step4.png) 
5. [Press Authorize](step5.png)
6. [Copy and save the refresh token](step6.png) (Save one per character)

Steps for later authorization and setup:
1. [Paste refresh token into Refresh Token](refresh1.png)
2. [Press Refresh Authorization](refresh2.png)

Steps for use:
1. [Enter desired path length in the spinner](use1.png)
2. [Press start](use2.png)
3. [See waypoints in EVE Client](use3.png)
4. Fly along waypoints until you reach the end, then repeat from step one.

Notes about use:
-Recommended path length is 8 jumps.  Path lengths above 10 tend to take a long time, and 13 tends to cause the program to crash.
-Once a system is put on a path, the program remembers it and tries not to visit it again.  That effect is reset if the program is restarted.
