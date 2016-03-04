# Dynamic Exploration Route Planner (DERP)

Utilizes data provided by CREST to dynamically plan exploration routes optimised for profit and (relative) safety.

Steps for initial authorization and setup:<br>
1. Press EVE SSO Login<br>![](step1.PNG)<br>
2. Copy URL to the left<br>![](step2.PNG)<br>
3. Login and select character<br>
4. Enter the code from the URL into the SSO Login Code field<br>![](step4.PNG)<br>
5. Press Authorize<br>![](step5.PNG)<br>
6. Copy and save the refresh token<br>![](step6.PNG) (Save one per character)<br>
<p>
Steps for later authorization and setup:<br>
1. Paste refresh token into Refresh Token<br>![](refresh1.PNG)<br>
2. Press Refresh Authorization<br>![](refresh2.PNG)<br>
<p>
Steps for use:<br>
1. Enter desired path length in the spinner<br>![](use1.PNG)<br>
2. Press start<br>![](use2.PNG)<br>
3. See waypoints in EVE Client<br>![](use3.PNG)<br>
4. Fly along waypoints until you reach the end, then repeat from step one.<br>
<p>
Notes about use:<br>
-Recommended path length is 8 jumps.  Path lengths above 10 tend to take a long time, and 13 tends to cause the program to crash.<br>
-Once a system is put on a path, the program remembers it and tries not to visit it again.  That effect is reset if the program is restarted.
