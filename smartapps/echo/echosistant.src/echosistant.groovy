/*
 * EchoSistant - The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enabled Device.
 *		
 *		12/09/2016		Version 3.0.2	Major overhaul of UI and process (cotnt'd)
 *		12/09/2016		Version 3.0.1	Major overhaul of UI and process (in progress)
 *		??/??/2016		Version 3.0.0	Additions: Msg to Notify Tab in Mobile App, Push Msg, Complete Reconfigure of Profile Build, More Control of Dimmers, and Switches,
 *										Device Activity Alerts Page, Toggle control, Flash control for switches. 
 *										Bug fixes: Time out error resolved.
 *		11/23/2016		Version 2.0.1	Bug fix: Pre-message not showing correctly.  Set to default false.
 *		11/22/2016		Version 2.0.0	CoRE integration, Cont Commands per profile, Repeat Message per profile, one app and many bug fixes.
 *		11/20/2016		Version 1.2.0	Fixes: SMS&Push not working, calling multiple profiles at initialize. Additions: Run Routines and Switch enhancements
 *		11/13/2016		Version 1.1.1a	Roadmap update and spelling errors
 *		11/13/2016		Version 1.1.1	Addition - Repeat last message
 *		11/12/2016		Version 1.1.0	OAuth bug fix, additional debug actions, Alexa feedback options, Intent and Utterance file updates
 *										Control Switches on/off with delay off, pre-message "null" bug
 *		11/07/2016		Version 1.0.1f	Additional Debug messages and Alexa missing profile Response
 *		11/06/2016		Version 1.0.1d	Debug measures fixed
 *		11/06/2016		Version 1.0.1c  Debug measures added
 *		11/05/2016		Version 1.0.1b	OAuth Fix and Version # update 
 *		11/05/2016 		Version 1.0.1a	OAuth Log error	@ 11:46EST OAuth - Bobby
 *		11/05/2016		Version 1.0.1	OAuth error fix
 *		11/04/2016      Version 1.0		Initial Release
 *
 * ROADMAP
 * Alarms and Timers with Voice Feedback
 * External TTS
 * External SMS
 * Google Calendar
 * Personal Message Que per User
 *
 *
 *
 * Credits
 * Thank you to @MichaelS (creator of AskAlexa) for guidance and for letting me use his outstanding Wiki
 * and to begin this project using his code as a jump start.  Thanks goes to Keith @n8xd for his help with  
 * troubleshooting my lambda code. And a huge thank you to @SBDOBRESCU, the co-author of this project, for 
 * jumping on board and helping me expand this project into something more. 
 *
 *  Copyright 2016 Jason Headley
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
/**********************************************************************************************************************************************/

definition(
	name			: "EchoSistant${parent ? " - Profile" : ""}",
    namespace		: "Echo",
    author			: "JH",
	description		: "The Ultimate Voice and Text Messaging Assistant Using Your Alexa Enabled Device.",
	singleInstance	: true,
    parent: parent ? "Echo.EchoSistant" : null,
    category		: "My Apps",
	iconUrl			: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
	iconX2Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png",
	iconX3Url		: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant@2x.png")
/**********************************************************************************************************************************************/

/************************************************************************************************************
   PARENT PAGES
************************************************************************************************************/
preferences {
    page name: "pageMain"
    //Parent Pages    
    page name: "mainParentPage"
    	page name: "profiles"
    	page name: "about"
    	page name: "tokens"
    		page name: "pageConfirmation"
    		page name: "pageReset"
        page name: "devicesControlMain"

    //Profile Pages    
    page name: "mainProfilePage"
    	page name: "MsgPro"
    	page name: "SMS"
    page name: "DevPro"
    	page name: "devicesControl"
    	page name: "CoRE"    
    page name: "StaPro"
        page name: "FeedBack"
        page name: "Alerts"
	page name: "MsgConfig"
    	page name: "certainTime"   
}

def pageMain() { if (!parent) mainParentPage() else mainProfilePage() }

/***********************************************************************************************************************
    PARENT UI CONFIGURATION
***********************************************************************************************************************/
page name: "mainParentPage"
    def mainParentPage() {	
       dynamicPage(name: "mainParentPage", title:"", install: true, uninstall:false) {
            //go to Parent set up
            section ("") {
                href "profiles", title: "Profiles", description: profilesDescr(), state: completeProfiles(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Config.png"    
                href "about", title: "Settings and Security", description: settingsDescr(), state: completeSettings(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_About.png"
                href title: "EchoSistant Support", description: supportDescr() , state: completeProfiles(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/app-Echosistant.png",
                    url: "http://thingsthataresmart.wiki/index.php?title=EchoSistant"    
                }
            section ("") {
                //label title:"              Rename Echosistant", required:false, defaultValue: ""  
                paragraph "${textCopyright()}"
                }
         }
    }
page name: "profiles"
    def profiles() {
            dynamicPage (name: "profiles", title: "", install: false, uninstall: false) {
            if (childApps.size()) { 
                    section(childApps.size()==1 ? "One Profile configured" : childApps.size() + " Profiles configured" )
            }
            section("Create New Profiles"){
                    app(appName: "EchoSistant", namespace: "Echo", multiple: true, description: "Tap Here to Create a New Profile...")
            } 
        } 
    }

    def about(){
        dynamicPage(name: "about", uninstall: true) {
                section ("Directions, How-to's, and Troubleshooting") { 
                href url:"http://thingsthataresmart.wiki/index.php?title=EchoSistant", title: "EchoSistant Wiki", description: none
                }
                section("Debugging") {
                    input "debug", "bool", title: "Enable Debug Logging", default: false, submitOnChange: true 
                    if (debug) log.info "${textAppName()}\n${textVersion()}"
                    }
                section ("Apache License"){
                    input "ShowLicense", "bool", title: "Show License", default: false, submitOnChange: true
                    def msg = textLicense()
                        if (ShowLicense) paragraph "${msg}"
                    }
                section ("Security Tokens"){
                    paragraph ("Log into the IDE on your computer and navigate to the Live Logs tab. Leave that window open, come back here, and open this section")
                    input "ShowTokens", "bool", title: "Show Security Tokens", default: false, submitOnChange: true
                    if (ShowTokens) paragraph "The Security Tokens are now displayed in the Live Logs section of the IDE"
                    def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. OAuth may not be enabled. "+
                    "Go to the SmartApp IDE settings to enable OAuth."	
                    if (ShowTokens) log.info "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                    if (ShowTokens) paragraph "Access token:\n${msg}\n\nApplication ID:\n${app.id}"
                    }
                section ("Revoke/Renew Access Token & Application ID"){
                    href "tokens", title: "Revoke/Reset Security Access Token", description: none
                    }
                section ("EchoSistant Integrations.... Apps and Devices"){
                    input "showIntegration", "bool", title: "Enable Integrations", default: false, submitOnChange: true
                        if(showIntegration) {
                            href"CoRE", title: "Integrate CoRe...",
                            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_CoRE.png"
                            href "devicesControlMain", title: "Control These Devices...", description: ParConDescr() , state: completeParCon(),
                            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"            			
                    		paragraph ("Define increments for the devices that Alexa controls directly")
                            input "cLevel", "number", title: "Alexa Adjusts Light Level by (1-100%)...", defaultValue: 30, required: false
                            input "cLevelOther", "number", title: "Alexa adjusts other switches by (1-10)...", defaultValue: 2, required: false
                            input "cTemperature", "number", title: "Alexa adjusts temperature by (1-10 degrees)...", defaultValue: 1, required: false
                         }
                    }
                section("Tap below to remove the ${textAppName()} application.  This will remove ALL Profiles and the App from the SmartThings mobile App."){}
                }
	}      
page name: "tokens"
    def tokens(){
            dynamicPage(name: "tokens", title: "Security Tokens", uninstall: false){
                section(""){
                    paragraph "Tap below to Reset/Renew the Security Token. You must log in to the IDE and open the Live Logs tab before tapping here. "+
                    "Copy and paste the displayed tokens into your Amazon Lambda Code."
                    if (!state.accessToken) {
                        OAuthToken()
                        paragraph "You must enable OAuth via the IDE to setup this app"
                        }
                    }
                        def msg = state.accessToken != null ? state.accessToken : "Could not create Access Token. "+
                        "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth." 
                        log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                section ("Reset Access Token / Application ID"){
                    href "pageConfirmation", title: "Reset Access Token and Application ID", description: none
                    }
                }
            } 
page name: "pageConfirmation"
    def pageConfirmation(){
            dynamicPage(name: "pageConfirmation", title: "Reset/Renew Access Token Confirmation", uninstall: false){
                section {
                    href "pageReset", title: "Reset/Renew Access Token", description: "Tap here to confirm action - READ WARNING BELOW"
                    paragraph "PLEASE CONFIRM! By resetting the access token you will disable the ability to interface this SmartApp with your Amazon Echo."+
                    "You will need to copy the new access token to your Amazon Lambda code to re-enable access." +
                    "Tap below to go back to the main menu with out resetting the token. You may also tap Done above."
                    }
                section(" "){
                    href "mainParentPage", title: "Cancel And Go Back To Main Menu", description: none 
                    }
                }
            }
page name: "pageReset"
    def pageReset(){
            dynamicPage(name: "pageReset", title: "Access Token Reset", uninstall: false){
                section{
                    revokeAccessToken()
                    state.accessToken = null
                    OAuthToken()
                    def msg = state.accessToken != null ? "New access token:\n${state.accessToken}\n\n" : "Could not reset Access Token."+
                    "OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
                    paragraph "${msg}"
                    paragraph "The new access token and app ID are now displayed in the Live Logs tab of the IDE."
                    log.info "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
                }
                section(" "){ 
                    href "mainParentPage", title: "Tap Here To Go Back To Main Menu", description: none 
                    }
                }
            } 
page name: "devicesControlMain"    
    def devicesControlMain(){
        dynamicPage(name: "devicesControlMain", title: "Select Devices That Alexa Can Control",install: false, uninstall: false) {
            section ("Switches", hideWhenEmpty: true){
                input "cSwitches", "capability.switch", title: "Control These Switches...", multiple: true, required: false, submitOnChange: true
                }
            section ("Dimmers", hideWhenEmpty: true){
                input "cDimmers", "capability.switchLevel", title: "Control These Dimmers...", multiple: true, required: false , submitOnChange:true
            }
            section ("Thermostats", hideWhenEmpty: true){
                input "tstat", "capability.thermostat", title: "Control These Thermostat(s)...", multiple: true, required: false
            }
            section ("Locks", hideWhenEmpty: true){
                input "lock", "capability.lock", title: "Control These Lock(s)...", multiple: true, required: false
            }
            section ("Doors", hideWhenEmpty: true){
                input "doors", "capability.doorControl", title: "Control These Door(s)...)" , multiple: true, required: false, submitOnChange: true    
            }
        }
    }    
/***********************************************************************************************************************
    PROFILE UI CONFIGURATION
***********************************************************************************************************************/
def mainProfilePage() {	
    dynamicPage(name: "mainProfilePage", title:"I Want This Profile To...", install: true, uninstall: true) {
        //go to Profile set up
        section {
           	href "MsgPro", title: "Send Audio Messages...", description: MsgProDescr(), state: completeMsgPro(),
   				image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png" 
            href "SMS", title: "Send Text & Push Messages...", description: SMSDescr() , state: completeSMS(),
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
  			href "DevPro", title: "Execute Actions when Profile runs...", description: DevProDescr(), state: completeDevPro(),
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"            			
            href "Alerts", title: "Create Event Alerts...",description: AlertProDescr() , state: completeAlertPro(),
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Rest.png"
           	href "MsgConfig", title: "With These Global Message Options...", description: MsgConfigDescr() , state: completeMsgConfig(),
            	image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png" 
            }
        section ("Name and/or Remove this Profile") {
 		   	label title:"              Rename Profile ", required:false, defaultValue: "New Profile"  
        }    
	}
}
page name: "MsgPro"
    def MsgPro(){
        dynamicPage(name: "MsgPro", title: "Using These Media Devices...", uninstall: false){
             section ("Music Player", hideWhenEmpty: true){
                input "sonosDevice", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true,
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
                if (sonosDevice) {
                    input "volume", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                }
            }  
            section ("Speech Synthesis", hideWhenEmpty: true) {
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
                input "synthDevice", "capability.speechSynthesis", title: "On this Speech Synthesis Type Devices", multiple: true, 
                required: false, submitOnChange: true,
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
            }        
        }   
    }
page name: "SMS"
    def SMS(){
        dynamicPage(name: "SMS", title: "Send SMS and/or Push Messages...", uninstall: false) {
            section ("Text Messages" ) {
            input "sendContactText", "bool", title: "Enable Text Notifications to Contact Book (if available)", required: true, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
                if (sendContactText) input "recipients", "contact", title: "Send text notifications to (optional)", multiple: true, required: false
                    input "sendText", "bool", title: "Enable Text Notifications to non-contact book phone(s)", required: true, submitOnChange: true ,          
                        image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
                if (sendText){      
                    paragraph "You may enter multiple phone numbers separated by comma to deliver the Alexa message as a text and a push notification. E.g. 8045551122;8046663344"
                    input name: "sms", title: "Send text notification to (optional):", type: "phone", required: false
                }
            }    
            section ("Push Messages") {
            input "push", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png" 
                if (parent.debug) log.debug "disable TTS='${disableTts}" 
            input "notify", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Text.png"
            }        
        }        
    }
page name: "DevPro"
    def DevPro(){
        dynamicPage(name: "DevPro", uninstall: false) {
            section ("Trigger these lights and/or execute these routines when the Profile runs...") {
                href "devicesControl", title: "Select switches...", description: DevConDescr() , state: completeDevCon(),
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_devices.png"
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"
                def actions = location.helloHome?.getPhrases()*.label 
                if (actions) {
                    actions.sort()
                    if (parent.debug) log.info actions
            	}                
                input "runRoutine", "enum", title: "Select a Routine(s) to execute", required: false, options: actions, multiple: true, 
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Routines.png"

            }
        }
    }
page name: "devicesControl"
    def devicesControl() {
            dynamicPage(name: "devicesControl", title: "Select Devices to use with this profile",install: false, uninstall: false) {
                section ("Switches", hideWhenEmpty: true){
                    input "switches", "capability.switch", title: "Select Switches...", multiple: true, required: false, submitOnChange: true, description: completeDevPro() , state: completeMsgPro()
                        if (switches) input "switchCmd", "enum", title: "What do you want to do with these switches?", options:["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: false, required: false, submitOnChange:true
                        if (switchCmd) input "otherSwitch", "capability.switch", title: "...and these other switches?", multiple: true, required: false, submitOnChange: true
                        if (otherSwitch) input "otherSwitchCmd", "enum", title: "What do you want to do with these other switches?", options: ["on":"Turn on","off":"Turn off","toggle":"Toggle"], multiple: false, required: false, submitOnChange: true
                    	}
                section ("Dimmers", hideWhenEmpty: true){
                    input "dimmers", "capability.switchLevel", title: "Select Dimmers...", multiple: true, required: false , submitOnChange:true
                        if (dimmers) input "dimmersCmd", "enum", title: "Command To Send To Dimmers", options:["on":"Turn on","off":"Turn off","set":"Set level"], multiple: false, required: false, submitOnChange:true
                        if (dimmersCMD == "set" && dimmers) input "dimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false
                        if (dimmersLVL) input "otherDimmers", "capability.switchLevel", title: "Control These Dimmers...", multiple: true, required: false , submitOnChange:true
                        if (otherDimmers) input "otherDimmersCmd", "enum", title: "Command To Send To Dimmers", options:["on":"Turn on","off":"Turn off","set":"Set level"], multiple: false, required: false, submitOnChange:true
                        if (otherDimmersCMD == "set" && otherDimmers) input "otherDimmersLVL", "number", title: "Dimmers Level", description: "Set dimmer level", required: false
                }
                section ("Flash These Switches") {
					input "flashSwitches", "capability.switch", title: "These switches", multiple: true, required: false, submitOnChange:true
					if (flashSwitches) {
                    input "numFlashes", "number", title: "This number of times (default 3)", required: false, submitOnChange:true
					input "onFor", "number", title: "On for (default 1 second)", required: false, submitOnChange:true
					input "offFor", "number", title: "Off for (default 1 second)", required: false, submitOnChange:true
						}
                    }
                section("Turn on these switches and dimmers after a delay of..."){
                    input "sSecondsOn", "number", title: "Seconds?", defaultValue: none, required: false
                }
                section("And then turn them off after a delay of..."){
                    input "sSecondsOff", "number", title: "Seconds?", defaultValue: none, required: false
                }
            }
       }        
page name: "CoRE"
    def CoRE(){
            dynamicPage(name: "CoRE", uninstall: false) {
                section ("Welcome to the CoRE integration page"){
                    paragraph ("--- This integration is in place to enhance the"+
                    " communication abilities of EchoSistant and your SmartThings"+
                    " Home Automation Project. It is not intended"+
                    " for the control of HA devices."+
                    " --- CoRE integration is currently one way only. You can NOT "+
                    " trigger profiles from within CoRE. CoRE listens for a "+
                    " profile execution and then performs the programmed tasks."+
                    " --- Configuration is simple. In EchoSistant create your profile."+
                    " Then open CoRE and create a new piston. In the condition section"+
                    " choose 'EchoSistant Profile' as the trigger. Choose the appropriate"+
                    " profile and then finish configuring the piston."+
                    " --- When the profile is executed the CoRE piston will also execute.")
            }
        }
    }    
page name: "Alerts"
    def Alerts(){
        dynamicPage(name: "Alerts", uninstall: false) {
        section ("Switches and Dimmers", hideWhenEmpty: true) {
            input "ShowSwitches", "bool", title: "Switches and Dimmers", default: false, submitOnChange: true
            if (TheSwitch || audioTextOn || audioTextOff || speech1 || push1 || notify1 || music1) paragraph "Configured with Settings"
            if (ShowSwitches) {        
                input "TheSwitch", "capability.switch", title: "Choose Switches...", required: false, multiple: true, submitOnChange: true
                input "audioTextOn", "audioTextOn", title: "Play this message", description: "Message to play when the switch turns on", required: false, capitalization: "sentences"
                input "audioTextOff", "audioTextOff", title: "Play this message", description: "Message to play when the switch turns off", required: false, capitalization: "sentences"
                input "speech1", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
                input "music1", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music1) {
                    input "volume1", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying1", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg1", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg1) {
                	input "push1", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify1", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            	}
            }             
        }
        section("Doors and Windows", hideWhenEmpty: true) {
            input "ShowContacts", "bool", title: "Doors and Windows", default: false, submitOnChange: true
            if (TheContact || audioTextOpen || audioTextClosed || speech2 || push2 || notify2 || music2) paragraph "Configured with Settings"
            if (ShowContacts) {
                input "TheContact", "capability.contactSensor", title: "Choose Doors and Windows..", required: false, multiple: true, submitOnChange: true
                input "audioTextOpen", "textOpen", title: "Play this message", description: "Message to play when the door opens", required: false, capitalization: "sentences"
                input "audioTextClosed", "textClosed", title: "Play this message", description: "Message to play when the door closes", required: false, capitalization: "sentences"
                input "speech2", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
            	input "music2", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music2) {
                    input "volume2", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying2", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg2", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg2) {
                	input "push2", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify2", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            	}
            }
        }
        section("Locks", hideWhenEmpty: true) {
            input "ShowLocks", "bool", title: "Locks", default: false, submitOnChange: true
            if (TheLock || audioTextLocked || audioTextUnlocked || speech3 || push3 || notify3 || music3) paragraph "Configured with Settings"
            if (ShowLocks) {
                input "TheLock", "capability.lock", title: "Choose Locks...", required: false, multiple: true, submitOnChange: true
                input "audioTextLocked", "textLocked", title: "Play this message", description: "Message to play when the lock locks", required: false, capitalization: "sentences"
                input "audioTextUnlocked", "textUnlocked", title: "Play this message", description: "Message to play when the lock unlocks", required: false, capitalization: "sentences"
                input "speech3", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
            	input "music3", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music3) {
                    input "volume3", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying3", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg3", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg3) {
                	input "push3", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify3", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
                }
            }
        }
        section("Motion Sensors", hideWhenEmpty: true) {
            input "ShowMotion", "bool", title: "Motion Sensors", default: false, submitOnChange: true
            if (TheMotion || audioTextActive || audioTextInactive || speech4 || push4 || notify4 || music4) paragraph "Configured with Settings"
            if (ShowMotion) {
                input "TheMotion", "capability.motionSensor", title: "Choose Motion Sensors...", required: false, multiple: true, submitOnChange: true
                input "audioTextActive", "textActive", title: "Play this message", description: "Message to play when motion is detected", required: false, capitalization: "sentences"
                input "audioTextInactive", "textInactive", title: "Play this message", description: "Message to play when motion stops", required: false, capitalization: "sentences"
                input "speech4", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
            	input "music4", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music4) {
                    input "volume4", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying4", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg4", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg4) {
                	input "push4", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify4", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            		
                }
            }
        }
        section("Presence Sensors", hideWhenEmpty: true) {
        	input "ShowPresence", "bool", title: "Presence Sensors", default: false, submitOnChange: true
        	if (ThePresence || audioTextPresent || audioTextNotPresent || speech5 || push5 || notify5 || music5) paragraph "Configured with Settings"
            if (ShowPresence) {
                input "ThePresence", "capability.presenceSensor", title: "Choose Presence Sensors...", required: false, multiple: true, submitOnChange: true
                input "audioTextPresent", "textPresent", title: "Play this message", description: "Message to play when the Sensor arrives", required: false, capitalization: "sentences"
                input "audioTextNotPresent", "textNotPresent", title: "Play this message", description: "Message to play when the Sensor Departs", required: false, capitalization: "sentences"
                input "speech5", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
                input "music5", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music5) {
                    input "volume5", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying5", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg5", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg5) {
                	input "push5", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify5", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            	}
			}
		}
        section("Water Sensors", hideWhenEmpty: true) {
        	input "ShowWater", "bool", title: "Water Detectors", default: false, submitOnChange: true
        	if (TheWater || audioTextWet || audioTextDry || speech6 || push6 || notify6 || music6) paragraph "Configured with Settings"
            if (ShowWater) {
                input "TheWater", "capability.waterSensor", title: "Choose Water Sensors...", required: false, multiple: true, submitOnChange: true
                input "audioTextWet", "textWet", title: "Play this message", description: "Message to play when water is detected", required: false, capitalization: "sentences"
                input "audioTextDry", "textDry", title: "Play this message", description: "Message to play when is no longer detected", required: false, capitalization: "sentences"
                input "speech6", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
				input "music6", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music6) {
                    input "volume6", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying6", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg6", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg6) {
                	input "push6", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify6", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            	}
			}                
        }        
        section("Garage Doors", hideWhenEmpty: true) {
        	input "ShowGarage", "bool", title: "Garage Doors", default: false, submitOnChange: true
        	if (TheGarage || audioTextOpening || audioTextClosing || speech7 || push7 || notify7 || music7) paragraph "Configured with Settings"
            if (ShowGarage) {
                input "TheGarage", "capability.garageDoorControl", title: "Choose Garage Doors...", required: false, multiple: true, submitOnChange: true
                input "audioTextOpening", "textOpening", title: "Play this message", description: "Message to play when the Garage Door Opens", required: false, capitalization: "sentences"
                input "audioTextClosing", "textClosing", title: "Play this message", description: "Message to play when the Garage Door Closes", required: false, capitalization: "sentences" 
                input "speech7", "capability.speechSynthesis", title: "Message Player", required: false, multiple: true, submitOnChange: true
                input "music7", "capability.musicPlayer", title: "On this Sonos Type Devices", required: false, multiple: true, submitOnChange: true
                if (music7) {
                    input "volume7", "number", title: "Temporarily change volume", description: "0-100%", required: false
                    input "resumePlaying7", "bool", title: "Resume currently playing music after notification", required: false, defaultValue: false
                	}
                input "sendMsg7", "bool", title: "Send Push and/or Notifications", default: false, submitOnChange: true
                	if (sendMsg7) {
                	input "push7", "bool", title: "Send Push Notification (optional)", required: false, defaultValue: false, submitOnChange: true
            		input "notify7", "bool", title: "Send message to Mobile App Notifications Tab (optional)", required: false, defaultValue: false, submitOnChange: true
            		}
                }		
            }   
        }
    }
page name: "MsgConfig"
    def MsgConfig(){
        dynamicPage(name: "MsgConfig", title: "Configure Global Profile Options...", uninstall: false) {
            section ("Voice Message Options and Alexa Responses") {
            input "MsgOpt", "bool", title: "I want to use these configuration options...", defaultValue: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Media.png"
                if (MsgOpt) {
                    input "ShowPreMsg", "bool", title: "Pre-Message (plays on Audio Playback Device before message)", defaultValue: false, submitOnChange: true
                        if (ShowPreMsg) input "PreMsg", "Text", title: "Pre-Message...", defaultValue: none, submitOnChange: true, required: false 
                    input "Acustom", "bool", title: "Custom Response from Alexa...", defaultValue: false, submitOnChange: true
                        if (Acustom) input "outputTxt", "text", title: "Input custom phrase...", required: false, defaultValue: "Message sent,   ", submitOnChange: true
                    input "Arepeat", "bool", title: "Alexa repeats message to sender when sent...", defaultValue: false, submitOnChange: true
                        if (Arepeat) {			
                        if (Arepeat && Acustom){
                            paragraph "NOTE: only one custom Alexa response can"+
                            " be delivered at once. Please only enable Custom Response OR Repeat Message"
                            }				
                        }
                    input "AfeedBack", "bool", title: "Turn on to disable Alexa Feedback Responses (silence Alexa) Overrides all other Alexa Options...", defaultValue: false, submitOnChange: true
                        if (AfeedBack) {
                        if (parent.debug) log.debug "Afeedback = '${AfeedBack}"
                        }
                    input "disableTts", "bool", title: "Disable All spoken notifications (No voice output from the speakers or Alexa)", required: false, submitOnChange: true  
                    input "ContCmds", "bool", title: "Allow Alexa to prompt for additional commands after message is delivered...", defaultValue: false, submitOnChange: true
                    input "ContCmdsR", "bool", title: "Allow Alexa to prompt for additional commands after Repeat command is given...", defaultValue: false, submitOnChange: true
                    }
                }            	
            section ("Mode Restrictions") {
                input "modes", "mode", title: "Only when mode is", multiple: true, required: false, submitOnChange: true,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
                }        
            section ("Days - Audio only on these days"){	
                input "runDay", title: "Only on certain days of the week", multiple: true, required: false, submitOnChange: true,
                    "enum", options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
                    image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
                }
            section ("Time - Audio only during these times"){
                href "certainTime", title: "Only during a certain time", description: timeIntervalLabel ?: "Tap to set", state: timeIntervalLabel ? "complete" : null,
                image: "https://raw.githubusercontent.com/BamaRayne/Echosistant/master/smartapps/bamarayne/echosistant.src/Echosistant_Extra.png"
                }   
	        }
    	}
page name: "certainTime"
    def certainTime() {
        dynamicPage(name:"certainTime",title: "Only during a certain time", uninstall: false) {
            section("Beginning at....") {
                input "startingX", "enum", title: "Starting at...", options: ["A specific time", "Sunrise", "Sunset"], required: false , submitOnChange: true
                if(startingX in [null, "A specific time"]) input "starting", "time", title: "Start time", required: false, submitOnChange: true
                else {
                    if(startingX == "Sunrise") input "startSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    else if(startingX == "Sunset") input "startSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                }
            }
            section("Ending at....") {
                input "endingX", "enum", title: "Ending at...", options: ["A specific time", "Sunrise", "Sunset"], required: false, submitOnChange: true
                if(endingX in [null, "A specific time"]) input "ending", "time", title: "End time", required: false, submitOnChange: true
                else {
                    if(endingX == "Sunrise") input "endSunriseOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                    else if(endingX == "Sunset") input "endSunsetOffset", "number", range: "*..*", title: "Offset in minutes (+/-)", required: false, submitOnChange: true
                }
            }
        }
    }
/*************************************************************************************************************
   CREATE INITIAL TOKEN
************************************************************************************************************/
def OAuthToken(){
	try {
		createAccessToken()
		log.debug "Creating new Access Token"
	} catch (e) {
		log.error "Access Token not defined. OAuth may not be enabled. Go to the SmartApp IDE settings to enable OAuth."
	}
}
//************************************************************************************************************
mappings {
	path("/b") { action: [GET: "processBegin"] }
	path("/c") { action: [GET: "controlDevices"] }
    path("/t") {action: [GET: "processTts"]}
}

/************************************************************************************************************
		Begining Process
************************************************************************************************************/
def processBegin(){
    	log.debug "--Begin commands received--"
    
    def Ver = params.versionTxt 		
    def versionDate = params.versionDate
    
    def pMain = app.label
	def pContinue = "Yes"
    	if (debug){
        log.debug "Message received from Lambda with: (ver) = '${Ver}', (date) = '${versionDate}', and sent to Lambda: pMain = '${pMain}', pContinue = '${pContinue}'"
        }
    return ["pContinue":pContinue, "pMain":pMain]
}   
/************************************************************************************************************
		Base Process
************************************************************************************************************/
def installed() {
	if (debug) log.debug "Installed with settings: ${settings}"
    if (debug) log.trace "STappID = '${app.id}' , STtoken = '${state.accessToken}'"
    alertHandler(evt)
	initialize()
}
def updated() { 
	if (debug) log.debug "Updated with settings: ${settings}"
    initialize()
    alertHandler(evt)
    if (debug) log.info getProfileList()
}
def initialize() {
    alertHandler(evt)	
	if (!parent){
    	if (debug) log.debug "Initialize !parent"
        sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
    	state.lastMessage = null
		state.lastIntent  = null
    	state.lastTime  = null
		def children = getChildApps()
    		if (debug) log.debug "$children.size Profiles installed"
			children.each { child ->
			}
			if (!state.accessToken) {
        		if (debug) log.debug "Access token not defined. Attempting to refresh. Ensure OAuth is enabled in the SmartThings IDE."
                OAuthToken()
        		if (debug) log.debug "STappID = '${app.id}' , STtoken = '${state.accessToken}'" 
			}
	}
    else{
        if (debug) log.debug "Initialize else block"
        state.lastMessage = null
    	state.lastTime  = null
     }

}

/************************************************************************************************************
		Subscriptions
************************************************************************************************************/
def subscribeToEvents() {
    alertHandler(evt)
	if (runModes) {
		subscribe(runMode, location.currentMode, modeChangeHandler)
	}
    if (runDay) {
   		subscribe(runDay, location.day, location.currentDay)
	} 
}
def unsubscribeToEvents() {
	if (triggerModes) {
    	unsubscribe(location, modeChangeHandler)
    }
} 


/************************************************************************************************************
		CoRE Integration
************************************************************************************************************/
def getProfileList(){
		return getChildApps()*.label
		if (debug) log.debug "Refreshing Profiles for CoRE, ${getChildApps()*.label}"
}

def childUninstalled() {
	if (debug) log.debug "Profile has been deleted, refreshing Profiles for CoRE, ${getChildApps()*.label}"
    sendLocationEvent(name: "echoSistant", value: "refresh", data: [profiles: getProfileList()] , isStateChange: true, descriptionText: "echoSistant Profile list refresh")
}

/************************************************************************************************************
   TEXT TO SPEECH PROCESS (PARENT) 
************************************************************************************************************/
def processTts() {
		def ptts = params.ttstext 
   		def pintentName = params.intentName
        def ttsintentname = params.ttsintentname
            if (debug) log.debug "Message received from Lambda with: (ptts) = '${ptts}', (pintentName) = '${pintentName}', ttsintentname = '${ttsintentname}'"
        def outputTxt = ''
    	def pContCmds = "false"
        def pContCmdsR = "false"
        def dataSet = [ptts:ptts,pttx:pttx,pintentName:pintentName] 
		def controlData = [pCommands:pCommands,pProfiles:pProfiles,pintentName:pintentName] 
        def repeat = "repeat last message"
       	def pMainIntent ="assistant"
        	if (mainIntent){
            		pMainIntent = mainIntent
        	}
        	if (debug) log.debug "#4 Main intent being called = '${pMainIntent}'"  
        if (ptts==repeat) {
				if (pMainIntent == pintentName) {
                outputTxt = "The last message sent was," + state.lastMessage + ", and it was sent to, " + state.lastIntent + ", at, " + state.lastTime 
				}
                else {
                      	childApps.each { child ->
    						def cLast = child.label
            				if (cLast == pintentName) {
                        		if (debug) log.debug "Last Child was = '${cLast}'"  
                                def cLastMessage 
                       			def cLastTime
                                pContCmds = child.ContCmds
                                pContCmdsR = child.ContCmdsR
                                if (pContCmdsR == false) {
                                	pContCmds = pContCmdsR
                                }
                                outputTxt = child.getLastMessage()
                                if (debug) log.debug "Profile matched is ${cLast}, last profile message was ${outputTxt}" 
                			}
               		}
               }
        }    
		else {
    			if (ptts){
     				state.lastMessage = ptts
                    state.lastIntent = pintentName
                    state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
                    if (debug) log.debug "Running main loop with '${ptts}'"                      
                    childApps.each {child ->
						child.profileEvaluate(dataSet)
            			//child.profileControl(controlData)
                        }
            			childApps.each { child ->
    						def cm = child.label
            					if (cm == pintentName) {
                              		def cAcustom = child.Acustom
									def cArepeat = child.Arepeat
									def cAfeedBack = child.AfeedBack
                                    pContCmds = child.ContCmds
     	                			if (cAfeedBack != true) {
                                		if (cAcustom != false) {
                            				outputTxt = child.outputTxt
                            			}
                            			else {
                        					if (cArepeat == !false || cArepeat == null ) {
                                            	outputTxt = "I have delivered the following message to '${cm}',  " + ptts
											}
                        					else {
                            					outputTxt = "Message sent to ${pintentName}, " 
												if (debug) log.debug "Alexa verbal response = '${outputTxt}'"
           									}
                                		}
                             		}
           						}  
                  		}
				}
      	}
        if (debug) log.debug "#6 Alexa response sent to Lambda = '${outputTxt}', '${pContCmds}' "
		return ["outputTxt":outputTxt, "pContCmds":pContCmds]
}

/************************************************************************************************************
   CONTROL PROCESS PROCESS (PARENT) 
************************************************************************************************************/
def controlDevices() {
        def pCommand = params.pCommand
        def pProfile = params.pProfile
        def pNum = params.pNum
        def pDevice = params.pDevice
		def controlData = [pCommand:pCommand,pProfile:pProfile,pNum:pNum,pDevice:pDevice] 
        
	if (debug) log.debug "Message received from Lambda to control devices with settings: (pCommand) = '${pCommand}', (pProfile) = '${pProfile}', pNum = '${pNum}', (pDevice) = '${pDevice}'"
}

/******************************************************************************************************
   SPEECH AND TEXT PROCESSING (PROFILE)
******************************************************************************************************/
def profileEvaluate(params) {
        def tts = params.ptts
        def txt = params.pttx
        def intent = params.pintentName        
        def childName = app.label       
        def data = [args: tts ]
        if (intent == childName){
        	sendLocationEvent(name: "echoSistantProfile", value: app.label, data: data, displayed: true, isStateChange: true, descriptionText: "EchoSistant activated '${app.label}' profile.")
      		if (parent.debug) log.debug "sendNotificationEvent sent to CoRE was '${app.label}' from the TTS process section"
            if (!disableTts){
        			if (PreMsg) 
        				tts = PreMsg + tts
        				if (parent.debug) log.debug "#3 tts = '${tts}'"
                    else {
            			tts = tts
            			if (parent.debug) log.debug "#4 tts = '${tts}'"
                    }
            			if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
            					if (synthDevice) {
                                synthDevice?.speak(tts) 
        			    			if (parent.debug) log.debug "#5 Sending message to Synthesis Devices" 
                                    }
                				if (mediaDevice) {
                                	mediaDevice?.speak(tts) 
                					if (parent.debug) log.debug "#6 Sending message to Media Devices"  
                                    }
            						if (tts) {
										state.sound = textToSpeech(tts instanceof List ? tts[0] : tts)
									}
									else {
										state.sound = textToSpeech("You selected the custom message option but did not enter a message in the $app.label Smart App")
									}
								if (sonosDevice) {
										sonosDevice.playTrackAndResume(state.sound.uri, state.sound.duration, volume)
                    						if (parent.debug) log.debug "Sending message to Sonos Devices" 
                                            }
    								}
    					sendtxt(txt) 
                        state.lastMessage = tts
                        state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
            	if (parent.debug) log.debug "Sending sms and voice message to selected phones and speakers"  
				}
				else {
    					sendtxt(txt)
                        state.lastMessage = txt
                        state.lastTime = new Date(now()).format("h:mm aa", location.timeZone)
           					if (parent.debug) log.debug "Only sending sms because disable voice message is ON"  
				}
                if (flashSwitches) {
                flashLights()
                }
                deviceControl()
                if (runRoutine) {
                location.helloHome?.execute(settings.runRoutine)
                }
                
        }
}

/******************************************************************************************************
   CONTROL PROCESSING (PROFILE)
******************************************************************************************************/
def profileControl(params) {

        def intent = params.pintentName
        def profile = params.pProfiles
        def command = params.pCommands
        def childName = app.label       
        if (parent.debug) log.debug "Message received from Parent with: (profile) = '${profile}', (command) = '${command}', intent = '${intent}', childName = '${childName}' "
        
        if (profile.toLowerCase()  == childName.toLowerCase()){
			if (parent.debug) log.debug "Profile called is '${profile}' with command '${command}'"
 		

        if (command == "dark" || command == "brighter" || command == "on") {
            if (sSecondsOn) {
            	runIn(sSecondsOn,turnOnSwitch)
                runIn(sSecondsOn,turnOnOtherSwitch)
                runIn(sSecondsOn,turnOnDimmers)
                runIn(sSecondsOn,turnOnOtherDimmers)
                }
             else {
       	    	if (parent.debug) log.debug "Turning switches on"
                switches?.on()
			}
        }
		if (command == "bright" || command == "darker" || command == "off") {     	
        	if (sSecondsOff) {
          		runIn(sSecondsOff,turnOffSwitch)
                runIn(sSecondsOff,turnOffOtherSwitch)
                runIn(sSecondsOff,turnOffDimmers)
                runIn(sSecondsOff,turnOffOtherDimmers)
			}	
        	else {
        		if (parent.debug) log.debug "Turning switches off"
                switches?.off()
            }
        }
	}
}

/***********************************************************************************************************************
    LAST MESSAGE HANDLER
***********************************************************************************************************************/
def getLastMessage() {
	def cOutputTxt = "The last message sent to " + app.label + " was," + state.lastMessage + ", and it was sent at, " + state.lastTime
	return  cOutputTxt 
	if (parent.debug) log.debug "Sending last message to parent '${cOutputTxt}' "
}
/***********************************************************************************************************************
    RESTRICTIONS HANDLER
***********************************************************************************************************************/
private getModeOk() {
    def result = !modes || modes?.contains(location.mode)
	result
} 
private getDayOk() {
    def df = new java.text.SimpleDateFormat("EEEE")
		def timeZone = location.timeZone
        location.timeZone ? df.setTimeZone(location.timeZone) : df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		def day = df.format(new Date())
	    def result = !runDay || runDay?.contains(day)
        def mode = location.mode
        if (parent.debug) log.trace "modeOk = $result; Location Mode is: $mode"
        if (parent.debug) log.trace "getDayOk = $result. Location time zone is: $timeZone"
        return result
}
private getTimeOk() {
	def result = true
	if ((starting && ending) ||
	(starting && endingX in ["Sunrise", "Sunset"]) ||
	(startingX in ["Sunrise", "Sunset"] && ending) ||
	(startingX in ["Sunrise", "Sunset"] && endingX in ["Sunrise", "Sunset"])) {
		def currTime = now()
		def start = null
		def stop = null
		def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: startSunriseOffset, sunsetOffset: startSunsetOffset)
		if(startingX == "Sunrise") start = s.sunrise.time
		else if(startingX == "Sunset") start = s.sunset.time
		else if(starting) start = timeToday(starting,location.timeZone).time
		s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: endSunriseOffset, sunsetOffset: endSunsetOffset)
		if(endingX == "Sunrise") stop = s.sunrise.time
		else if(endingX == "Sunset") stop = s.sunset.time
		else if(ending) stop = timeToday(ending,location.timeZone).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	if (parent.debug) log.trace "getTimeOk = $result."
    }
    return result
}
private hhmm(time, fmt = "h:mm a") {
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}
private offset(value) {
	def result = value ? ((value > 0 ? "+" : "") + value + " min") : ""
}
private timeIntervalLabel() {
	def result = ""
	if      (startingX == "Sunrise" && endingX == "Sunrise") result = "Sunrise" + offset(startSunriseOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunrise" && endingX == "Sunset") result = "Sunrise" + offset(startSunriseOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunset" && endingX == "Sunrise") result = "Sunset" + offset(startSunsetOffset) + " to Sunrise" + offset(endSunriseOffset)
	else if (startingX == "Sunset" && endingX == "Sunset") result = "Sunset" + offset(startSunsetOffset) + " to Sunset" + offset(endSunsetOffset)
	else if (startingX == "Sunrise" && ending) result = "Sunrise" + offset(startSunriseOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (startingX == "Sunset" && ending) result = "Sunset" + offset(startSunsetOffset) + " to " + hhmm(ending, "h:mm a z")
	else if (starting && endingX == "Sunrise") result = hhmm(starting) + " to Sunrise" + offset(endSunriseOffset)
	else if (starting && endingX == "Sunset") result = hhmm(starting) + " to Sunset" + offset(endSunsetOffset)
	else if (starting && ending) result = hhmm(starting) + " to " + hhmm(ending, "h:mm a z")
}
/***********************************************************************************************************************
    SMS HANDLER
***********************************************************************************************************************/
private void sendText(number, message) {
    if (sms) {
        def phones = sms.split("\\,")
        for (phone in phones) {
            sendSms(phone, message)
        }
    }
}
private void sendtxt(message) {
    if (parent.debug) log.debug message
    if (sendContactText) { 
        sendNotificationToContacts(message, recipients)
    } 
    if (push || push1 || push2 || push3 || push4 || push5 || push6 || push7) { 
    sendPush message 
    } 

    if (notify || notify1 || notify2 || notify3 || notify4 || notify5 || notify6 || notify7) {
        sendNotificationEvent(message)
    }
    if (sms) {
        sendText(sms, message)
	}
}
/************************************************************************************************************
   Switch/Dimmer/Toggle Handlers
************************************************************************************************************/

def switchOnHandler(evt) {
    log.debug "switchOnHandler called: $evt"
    switches?.on()
}
def switchOffHandler(evt) {
    log.debug "switchOffHandler called: $evt"
	switches?.off()
}

def turnOnSwitch() {
		if (intent == childName){
			switches?.on()
    		otherSwitch?.on()
    		dimmers?.on()
    		otherDimmers?.on()
		}   
}

def turnOffSwitch() {
		if (intent == childName){
			switches?.off()
    		otherSwitch?.off()
    		dimmers?.off()
    		otherDimmers?.off()
		}   
} 
private deviceControl() {
        if (intent == childName){
            if (sSecondsOn) {
            if (parent.debug) log.debug "Turn switches on in '${sSecondsOn}' seconds"
            	runIn(sSecondsOn,turnOnSwitch)
                runIn(sSecondsOn,turnOnOtherSwitch)
                runIn(sSecondsOn,turnOnDimmers)
                runIn(sSecondsOn,turnOnOtherDimmers)
                }
        	if (!sSecondsOn) {
            	if  (switchCmd == "on") {
	            	switches?.on()
                    }
           		else if (switchCmd == "off") {
	           		switches?.off()
                    }
                if (switchCmd == "toggle") {
                  	toggle()
                    }
          	if (otherSwitchCmd == "on") {
            	otherSwitch?.on()
                }
            	else if (otherSwitchCmd == "off") {
                	otherSwitch?.off()
                	}
          	if (dimmersCmd == "set" && dimmers){
				if (dimmersCmd == "set"){
        			def level = dimmersLVL < 0 || !dimmersLVL ?  0 : dimmersLVL >100 ? 100 : dimmersLVL as int
        			dimmers?.setLevel(level)
            		}
				}                    
            if (otherDimmersCmd == "set" && otherDimmers){
				if (otherDimmersCmd == "set"){
        		def otherlevel = otherDimmersLVL < 0 || !otherDimmersLVL ?  0 : otherDimmersLVL >100 ? 100 : otherDimmersLVL as int
        		otherDimmers?.setLevel(otherlevel)
				}
        	}            
		}
                if (sSecondsOff) {
            	if (parent.debug) log.debug "Turn switches off in '${sSecondsOff}' seconds"            
          		runIn(sSecondsOff,turnOffSwitch)
                runIn(sSecondsOff,turnOffOtherSwitch)
                runIn(sSecondsOff,turnOffDimmers)
                runIn(sSecondsOff,turnOffOtherDimmers)
			}
		}
	}        
def toggle() {
	if (parent.debug) log.debug "The selected device is toggling now"
	if (switches) {
	if (switches?.currentValue('switch').contains('on')) {
		switches?.off()
		}
    else if (switches?.currentValue('switch').contains('off')) {
		switches?.on()
		}
    }
    if (otherSwitch) {
	if (otherSwitch?.currentValue('switch').contains('on')) {
		otherSwitch?.off()
	}
	else if (otherSwitch?.currentValue('switch').contains('off')) {
		otherSwitch?.on()
		}
	}
	if (lock) {
	if (lock?.currentValue('lock').contains('locked')) {
		lock?.unlock()
		}
    }
if (parent.debug) log.debug "The selected device has toggled"
}
/************************************************************************************************************
   Flashing Lights Handler
************************************************************************************************************/
private flashLights() {
	if (parent.debug) log.debug "The Flash Switches Option has been activated"
	def doFlash = true
	def onFor = onFor ?: 60000/60
	def offFor = offFor ?: 60000/60
	def numFlashes = numFlashes ?: 3
	if (state.lastActivated) {
		def elapsed = now() - state.lastActivated
		def sequenceTime = (numFlashes + 1) * (onFor + offFor)
		doFlash = elapsed > sequenceTime
	}
	if (doFlash) {
		state.lastActivated = now()
		def initialActionOn = flashSwitches.collect{it.currentflashSwitch != "on"}
		def delay = 0L
		numFlashes.times {
			flashSwitches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.on(delay: delay)
				}
				else {
					s.off(delay:delay)
				}
			}
			delay += onFor
			flashSwitches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.off(delay: delay)
				}
				else {
					s.on(delay:delay)
				}
			}
			delay += offFor
		}
	}
}
/************************************************************************************************************
   Alerts Handler
************************************************************************************************************/
def myHandler(evt) {
if (getDayOk()==true && getModeOk()==true && getTimeOk()==true) {
     if ("on" == evt.value) {
     	if (audioTextOn) {
  		speech1?.speak(audioTextOn)
        music1?.play(audioTextOn)        
   			}
        }
    	if ("off" == evt.value) {
        	if (audioTextOff) {
        	speech1?.speak(audioTextOff)
            music1?.play(audioTextOff)
   				}
            }
    if ("open" == evt.value) {
    	if (audioTextOpen) {
  		speech2?.speak(audioTextOpen)
        music2?.play(audioTextOpen)
    		}
        }
    	if ("closed" == evt.value) {
        	if (audioTextClosed) {
        	speech2?.speak(audioTextClosed)
            music2?.play(audioTextClosed)
    			}
            }
    if ("locked" == evt.value) {
    	if (audioTextLocked) {
    	speech3?.speak(audioTextLocked)
        music3?.play(audioTextLocked)
    		}
        }
    	if ("unlocked" == evt.value) {
        	if (audioTextUnlocked) {
        	speech3?.speak(audioTextUnlocked)
            music3?.play(audioTextUnlocked)
        		}
            }
    if ("active" == evt.value) {
    	if (audioTextActive) {
    	speech4?.speak(audioTextActive)
        music4?.play(audioTextActive)
    		}
        }
    	if ("inActive" == evt.value)  {
        	if (audioTextInactive) {
        	speech4?.speak(audioTextInactive)
            music4?.play(audioTextInactive)
        		}
            }
    if ("present" == evt.value) {
    	if (audioTextPresent) {
    	speech5?.speak(audioTextPresent)
        music5?.play(audioTextPresent)
    		}
        }
    	if ("notPresent" == evt.value)  {
        	if (audioTextNotPresent) {
        	speech5?.speak(audioTextNotPresent)
            music5?.play(audioTextNotPresent)
        		}
            }
    if ("dry" == evt.value) {
    	if (audioTextDry) {
    	speech6?.speak(audioTextDry)
        music6?.play(audioTextDry)
    		}
        }
    	if ("Wet" == evt.value) {
        	if (audioTextWet) {
        	speech6?.speak(audioTextWet)
            music6?.play(audioTextWet)
        		}
            }
    if ("opening" == evt.value) {
    	if (audioTextOpening) {
    	speech7?.speak(audioTextOpening)
        music7?.play(audioTextOpening)
    		}
        }
    	if ("Closing" == evt.value) {
        	if (audioTextClosing) {
        	speech7?.speak(audioTextClosing)
            music7?.play(audioTextClosing)
	  		}
        }
	}
}
def alertHandler(evt) {
if (TheSwitch) {
	subscribe(TheSwitch, "switch", myHandler)
    }
if (TheContact) {
	subscribe(TheContact, "contact.open", myHandler)
    subscribe(TheContact, "contact.closed", myHandler)
    }
if (TheLock) {
    subscribe(TheLock, "lock.locked", myHandler)
    subscribe(TheLock, "lock.unlocked", myHandler)
	}
if (TheMotion) {
    subscribe(TheMotion, "motion.active", myHandler)
    subscribe(TheMotion, "motion.inactive", myHandler)
    }
if (ThePresence) {
    subscribe(ThePresence, "presence.present", myHandler)
    subscribe(ThePresence, "presence.notPresent", myHandler)
    }
if (TheWater) {    
    subscribe(TheWater, "waterSensor.dry", myHandler)
    subscribe(TheWater, "waterSensor.wet", myHandler)
    }
}
/************************************************************************************************************
   Version/Copyright/Information/Help
************************************************************************************************************/
private def textAppName() {
	def text = app.label
}	
private def textVersion() {
	def text = "Version 3.0.0 Alpha (11/30/2016)"
}
private def textCopyright() {
	def text = "       Copyright © 2016 Jason Headley"
}
private def textLicense() {
	def text =
	"Licensed under the Apache License, Version 2.0 (the 'License'); "+
	"you may not use this file except in compliance with the License. "+
	"You may obtain a copy of the License at"+
	"\n\n"+
	" http://www.apache.org/licenses/LICENSE-2.0"+
	"\n\n"+
	"Unless required by applicable law or agreed to in writing, software "+
	"distributed under the License is distributed on an 'AS IS' BASIS, "+
	"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "+
	"See the License for the specific language governing permissions and "+
	"limitations under the License."
}

private textProfiles() {
def text = childApps.size()     
}

private def textHelp() {
	def text =
		"This smartapp allows you to use an Alexa device to generate a voice or text message on on a different device"
        "See our Wikilinks page for user information!"
		}
        
/************************************************************************************************************
   Page status and descriptions 
************************************************************************************************************/       
//go to Parent set up 
def completeProfiles(){
    def result = ""
    if (childApps.size()) {
    	result = "complete"	
    }
    result
}
def profilesDescr() {
    def text = "No Profiles have been configured. Tap here to begin"
    def ch = childApps.size()     

    if (ch == 1) {
        text = "One profile has been configured. Tap here to change its settings or add more Profiles"
    }
    else {
    	if (ch > 1) {
        text = "${ch} Profiles have been configured. Tap here to change their settings or add more Profiles"
     	}
    }
    
    text
}
def completeSettings(){
    def result = ""
    if (ShowTokens || debug || ShowLicense) {
    	result = "complete"	
    }
    result
}
def settingsDescr() {
    def text = "Tap here to configure settings"
    if (debug && ShowTokens) {
    	text = "Logging and Show License are enabled; details are displayed in the Live Logs section of the IDE. Tap here to change this and other general settings." 
        }
    else {
    	if (ShowTokens) {
    		text = "Attention: Security Tokens are displayed in the Live Logs section of the IDE. Tap here to change this and other general settings"  
        }
   		else {
        	if (ShowLicense) {
			text = "License information is displayed on the next page"         
			}
    		if (debug) {
    		text = "Logging is enabled, view results in the IDE Live Logs"
        	}
		}
    }
   text
}
def supportDescr()  {
	def text = "${textVersion()}\n Click to visit our Wiki Page"
}
def DevProDescr() {
    def text = "Tap here to Configure"
	
    if (switches || dimmers || runRoutine) { 
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)" 
    }
    text
}


def DevConDescr() {
	def text = "Tap to set"
     if (switches || dimmers)
     { 
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)"
            }
    text   
}
def ParConDescr() {
	def text = "Tap to set"
     if (cSwitches || cDimmers || tstat || lock || doors)
     { 
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)"
            }
    text   
}       
def completeParCon() {
    def result = ""
    if (cSwitches || cDimmers || tstat || lock || doors) { 
       result = "complete"
    }
    result
}
     
//go to Profile set up
def completeMsgPro(){
    def result = ""
    if (synthDevice || sonosDevice) {
    	result = "complete"	
    }
    result
}
def MsgProDescr() {
    def text = "Tap here to Configure"
	
    if (synthDevice || sonosDevice) {
        if (synthDevice && !sonosDevice) {   
            text = "Configured"//"Using: ${synthDevice}. Tap to change device(s)" 
        }
        if (!synthDevice && sonosDevice) {
            text = "Configured" //"Using: ${sonosDevice}. Tap to change device(s)" 
        }
        if (synthDevice && sonosDevice) {
            text = "Configured" //"Using: ${synthDevice} AND ${synthDevice}. Tap to change device(s)" 
        }
     }
    text
}
def completeSMS(){
    def result = ""
    if (sendContactText || sms || push) {
    	result = "complete"	
    }
    result
}
def SMSDescr() {
    def text = "Tap here to Configure"
	
    if (sendContactText || sms || push) {
            text = "Configured" //"Using this contact(s): ${recipients}. Tap to change" 
     }
    text
}
def completeDevPro(){
    def result = ""
    if (switches || dimmers || runRoutine) {
    	result = "complete"	
    }    
    result
}
def completeDevCon() {
    def result = ""
    if (switches || dimmers) { 
       result = "complete"
    }
    result
}

def completeAlertPro(){
	def result = ""
	if (speech1 || push1 || notify1 || music1 || speech2 || push2 || notify2 || music2 || speech3 || push3 || notify3 || music3 || speech4 || push4 || notify4 || music4 || speech5 || push5 || notify5 || music5 || speech6 || push6 || notify6 || music6 || speech7 || push7 || notify7 || music7) 
    {
    result = "complete"
    }    	
    	result
}
def AlertProDescr() {
    def text = "Tap here to Configure"
	if (speech1 || push1 || notify1 || music1 || speech2 || push2 || notify2 || music2 || speech3 || push3 || notify3 || music3 || speech4 || push4 || notify4 || music4 || speech5 || push5 || notify5 || music5 || speech6 || push6 || notify6 || music6 || speech7 || push7 || notify7 || music7) 
    {
    text = "Configured"
    }
	    text
}
def completeMsgConfig(){
    def result = ""
    if (ShowPreMsg || Acustom || Arepeat || AfeedBack || disableTts || ContCmds || ContCmdsR || modes || runDay) {
    	result = "complete"	
    }
    result
}
def MsgConfigDescr() {
    def text = "Tap here to Configure"
	if (ShowPreMsg || Acustom || Arepeat || AfeedBack || disableTts || ContCmds || ContCmdsR) {
    	text = "Configured with Message Options"
        }
        if (getDayOk()==false || getModeOk()==false || getTimeOk()==false) {
            text = "Configured with restrictions. Tap to change" 
        }
    text
}

def runRoutineDescr() {
    def text = "Tap here to Configure"
	
    if (runRoutine) { 
            text = "Configured" //"These devices will execute: ${switches}, ${dimmers}. Tap to change device(s)" 
    }
    text
}



