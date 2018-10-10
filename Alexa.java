
package com.Alexa;
/*
 * Author: Jessica Van Brummelen
 * Email: jess AT csail.mit.edu
 */

// todo: can we do this?
// import java.util.ArrayList;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.*;

import android.util.Log;

@DesignerComponent(
    version = Alexa.VERSION,
    description =
        "An extension to connect with Amazon Alexa enabled devices."
        +
        "This extension can generate JSON and JavaScript to be used in the Amazon Developer Console. "
        +
        "After implementing the code in the Amazon Developer Console, this extension can listen for a signal from Alexa.",
    category = ComponentCategory.EXTENSION, nonVisible = true,
    iconName = "appengine/src/com/google/appinventor/images/alexa.png")
@SimpleObject(external = true)
public class Alexa extends AndroidNonvisibleComponent
    implements Component {
    //, OnCloudDBDataChangedListener { // this is for the actual implementation
  public static final int VERSION = 1;
  private ComponentContainer container;
  // todo: change to false
  private static final boolean DEBUG = true;
  private static final String LOG_TAG = "Alexa";
  // todo: make sure clouddb tags are NEVER set to this specific tag!!
  private static final String SIGNAL_TAG =
      "_ALEXA_SIGNAL_"; // tag from CloudDB to signal this alexa component
  // DEFAULTS:
  public static final String DEFAULT_INVOC_NAME = "Inventor Codi";
  // public static final ArrayList<String> DEFAULT_UTTERANCES = new ArrayList();
  // todo Private Variables:
  private CloudDB cloud;
  private String invocationName;
  // private ArrayList<String> utterances; // todo

  /**
   * Constructor creates a new Alexa object with default values.
   */
  public Alexa(ComponentContainer container) {
    super(container.$form());
    this.container = container;

    // Set defaults
    InvocationName(DEFAULT_INVOC_NAME);
    // todo Utterances(DEFAULT_UTTERANCES);

    // Register to listen to data changed events in CloudDB
    // todo del:
    if (DEBUG) {
      Log.d(LOG_TAG, "adding to clouddbchanged listeners. this: " + this +
                         "; form: " + form);
    }
    //form.registerForOnCloudDBDataChanged(this); // this is for the actual implementation

    // Create a CloudDB component to talk to the redis database (& connect to
    // Amazon Alexa)
    cloud = new CloudDB(container);
    // set up cloudDB:
    cloud.RedisServer("clouddb.appinventor.mit.edu");
    cloud.ProjectID("needs to be set in properties");
    cloud.Token("needs to be set in properties");
    cloud.Initialize();
  }

  /*
   * ******** Alexa Methods (Public) ********
   */
  /**
   * Called when there is a data change event in CloudDB.
   *
   * Checks if the tag from CloudDB is relevant to Alexa. If it is, this method
   * handles the call from Alexa and returns a value of true. If it is not
   * relevant, this method lets CloudDB handle the data change by returning
   * false.
   *
   * @param tag the tag String relevant to the value changed in CloudDB
   * @param val the value Object changed in CloudDB
   *
   * @return boolean value: if true, the change was handled; if false the change
   * still needs to be handled
   */
  public boolean onCloudDBDataChanged(final String tag, final Object val) {
    if (DEBUG) {
      Log.d(LOG_TAG,
            "onCloudDBDataChanged was called. Tag: " + tag + "; Value: " + val);
    }
    boolean handled = false;
    if (tag.contains(SIGNAL_TAG)) {
      if (DEBUG) {
        Log.d(LOG_TAG, "Tag is relevant to Alexa: " + tag + "; Value: " + val);
      }
      // handle the signal and return true
      this.ReceivedSignal(tag, val);
      handled = true;
    }
    return handled;
  }

  /**
   * Indicates that Alexa has signalled this App Inventor project.
   * Launches an event with the tag and value updated in CloudDB (as specified
   * in the Alexa Skill).
   *
   * @param tag the tag that Alexa changed in CloudDB.
   * @param value the new value of the tag.
   */
  @SimpleEvent
  public void ReceivedSignal(final String tag, final Object value) {
    if (DEBUG) {
      Log.d(LOG_TAG, "Received relevant Alexa signal. Tag: " + tag +
                         "; Value: " + value);
    }
    // Invoke the application's "ReceivedSignal" event handler
    EventDispatcher.dispatchEvent(Alexa.this, "ReceivedSignal", tag, value);
  }

  /**
   * TODO: delete this function. this is used for testing to set a value in
   * clouddb and pretend we're receiving it from alexa.
   */

  @SimpleFunction
  public void StoreValue(final String tag, final Object valueToStore) {
    if (DEBUG) {
      Log.d(LOG_TAG, "Storing a signal. UI thread: " + Thread.currentThread());
    }
    cloud.StoreValue(tag, valueToStore);
  }

  /*
   * ******** Helper Methods (Private) ********
   */

  /*
   * ******** Getters and Setters ********
   */
  // ---------------- Property Setters ----------------
  /**
   * Sets the Invocation Name used to open the skill in Amazon Alexa.
   *
   * @param invocationName The name used to open the Amazon Alexa Skill.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
                    defaultValue = Alexa.DEFAULT_INVOC_NAME + "")
  @SimpleProperty(
      description =
          "Sets the Invocation Name used to open the skill in Amazon Alexa.")
  public void
  InvocationName(String invocationName) {
    // todo: check for all the requirements for the invocationName on Amazon
    // Skill Developer.
    this.invocationName = invocationName;
  }
  // /**
  //  * Sets the Utterances used to open the skill in Amazon Alexa.
  //  *
  //  * @param utterances The name used to open the Amazon Alexa Skill.
  //  */
  // @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
  //                   defaultValue = Alexa.DEFAULT_INVOC_NAME + "")
  // @SimpleProperty(
  //     description =
  //         "Sets the Utterances used to open the skill in Amazon Alexa.")
  // public void
  // Utterances(ArrayList<String> utterances) {
  //   // todo: check for all the requirements for the utterances on Amazon
  //   // Skill Developer.
  //   this.utterances = utterances;
  // }

  /**
   * Specifies the ID of this Alexa project by calling CloudDB's ProjectID(id)
   * function.
   *
   * @param id a String of the project's ID
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
                    defaultValue = "")
  public void
  ProjectID(String id) {
    if (id.equals("")) {
      id = "ID is blank. Please set.";
    }
    if (DEBUG) {
      Log.d(LOG_TAG, "Setting project ID to: " + id);
    }
    cloud.ProjectID(id);
  }

  /**
   * Specifies the Token Signature of this this Alexa project by calling
   * CloudDB's Token(authToken).
   *
   * @param authToken for CloudDB server
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
                    defaultValue = "")
  public void
  Token(String authToken) {
    if (authToken.equals("")) {
      authToken = "authToken is blank. Please set.";
    }
    if (DEBUG) {
      Log.d(LOG_TAG, "Setting authToken to: " + authToken);
    }
    cloud.Token(authToken);
  }

  // ---------------- Property Getters ----------------
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR,
      description =
          "Gets the Invocation Name used to open the skill in Amazon Alexa.")
  public String
  InvocationName() {
    return this.invocationName;
  }
  // @SimpleProperty(
  //     category = PropertyCategory.BEHAVIOR,
  //     description =
  //         "Gets the Utterances used to open the skill in Amazon Alexa.")
  // public ArrayList<String>
  // Utterances() {
  //   return this.utterances;
  // }

  /**
   * Getter for the ProjectID. This ID comes from the CloudDB component embedded
   * in the Alexa component (i.e., "cloud").
   *
   * @return the ProjectID for this CloudDB project
   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,
                  description = "Gets the ProjectID for this Alexa project.")
  public String
  ProjectID() {
    String id = cloud.ProjectID();
    if (DEBUG) {
      Log.d(LOG_TAG, "Project ID: " + id);
    }
    return id;
  }

  /**
   * Getter for the authTokenSignature. This signature comes from the CloudDB
   * component embedded in the Alexa component (i.e., "cloud").
   *
   * @return the authTokenSignature for this CloudDB project
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR, userVisible = false,
      description =
          "This field contains the authentication token used to login to "
          +
          "the backend Redis server. The app won't run unless a token is specified."
          +
          "To specify a working token, create a CloudDB component by dragging it into "
          +
          "your project from the Experimental section, and copy the token there into "
          + "this Alexa component.")
  public String
  Token() {
    String token = cloud.Token();
    if (DEBUG) {
      Log.d(LOG_TAG, "Token: " + token);
    }
    return token;
  }
}
