
package com.google.appinventor.components.runtime;

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
    version = ExtensionTemplate.VERSION, 
    description =
        "A template for creating extensions.",
    category = ComponentCategory.EXTENSION, nonVisible = true,
    iconName = "appengine/src/com/google/appinventor/images/alexa.png")  // change this to whatever icon you want for the extension
@SimpleObject(external = true)
public class ExtensionTemplate extends AndroidNonvisibleComponent implements Component {
  public static final int VERSION = 1;
  private ComponentContainer container;

  // example variable for extension
  private String exampleVar;

  // defaults:
  public static final String DEFAULT_EXAMPLE_VAR_VALUE = "default";
  
  /**
   * Constructor creates a new extension object with default values.
   */
  public ExtensionTemplate(ComponentContainer container) {
    super(container.$form());
    this.container = container;
  }
  
  /*
   * ******** Helper Methods (Private) ********
   */

  // example method to show how to return a value from a method
  private boolean exampleReturnTrue() {
	  return true;
  }

  // example method to show how to write a method with params
  private int exampleEvaluateParams(int x, int y) {
          return x + y;
  }

  /*
   * ******** Getters and Setters ********
   */

  // example for setting a variable value
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,  // change to what type of variable you have
                    defaultValue = ExtensionTemplate.DEFAULT_EXAMPLE_VAR_VALUE)
  @SimpleProperty(
      description = "Test variable")
  public void ExampleVar(String exampleVar) {
    this.exampleVar = exampleVar;
  }

  // example for getting a variable value
  @SimpleProperty(
    category = PropertyCategory.BEHAVIOR,     // set depending on what type of variable this is
    description = "Gets value of exampleVar")
  public String ExampleVar() {
	  return this.exampleVar;
  }
}
