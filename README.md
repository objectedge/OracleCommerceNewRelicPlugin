# Oracle Commerce NewRelic Plugin Community Edition
-----------------------
An Oracle Commerce module for gathering health information from ATG servers.

## Requirements
-----------------------
* Oracle Commerce 10.1.2 or higher
* Java Runtime (JRE) environment Version 1.6 or later.
* [Oracle Commerce NewRelic Plugin Agent ](https://github.com/objectedge/OracleCommerceNewRelicPluginAgent.git)
* A New Relic account. Sign up for a free account [here](http://newrelic.com).
* Apache ant 1.9 or later

## How it works:
-----------------------
The ``OC Newrelic module`` starts along with Oracle Commerce server and monitors key health metrics. These health metrics are available to be queried using ``REST API`` on a real-time basis. 

The current version of ``OC Newrelic module`` allows monitoring of following metrics:
* Repository Cache Statistics
* SMTP Email Queue Length

The ``OC Newrelic module`` is easily extendable to add more monitoring specific to your needs.

The ``OC Newrelic module`` exposes the health metrics in a consistent and normalized format. The JSON format looks as follows:

###### JSON response format:

	{
	  "success": "true|false",
	  "status": "status code"
	  "message": "error|success message",
	  "components": [
	    { "name": "component-name",
	      "description": "component-description",
	      "metrics": [{"name": "metric-name", "unit": "metric-unit", "value": "value"}]
	    }
	  ]
	}
    
###### For Example:

	{
	  "success": "true",
	  "status": 200
	  "message": "OK",
	  "components": [
	    { "name": "/atg/commerce/catalog/Test",
	      "description": "Test component",
	      "metrics": [{"name": "testCount", "unit": "count", "value": 300}]
	    }
	  ]
	}


## Installation
-----------------------
#### Step 1 - Checkout the `OE.NewRelic` module
Checkout the module from this repository as a Java project into your favorite IDE.

#### Step 2 - Build the `OE.NewRelic` module
Set DYNAMO_HOME and DYNAMO_ROOT variables in the environment pointing to the local Oracle Commerce installation stack. Execute the build.xml (default target "all"). This will compile the classes and copy the artifacts into the Oracle Commerce installation stack.

#### Step 3 - Assemble the `OE.NewRelic` module
When assembling the .ear for your application, add `OE.NewRelic` to the list of modules to assemble before calling run-assembler command. 

**Note**: For more information refer [Oracle Commerce Platform Programming Guide](http://docs.oracle.com/cd/E24152_01/Platform.10-1/ATGPlatformProgGuide/html/s0304commandoptions01.html).

#### Step 4 - Start the `OE.NewRelic` module
When starting the server add 'OE.NewRelic' to the list of modules to start, 
for ex:
```
-Datg.dynamo.modules=Estore,OE.Newrelic
```

#### Step 5 - Ensure successful startup of the `OE.NewRelic` module

* Navigate to dyn/admin of the Oracle Commerce server and check for running products. You should see `OE.NewRelic` in the lis of running modules.
```
http://[servername]:[port]/dyn/admin/atg/dynamo/admin/en/running-products.jhtml
```

* Ensure that the REST API is providing health metrics. On a browser navigate to the following URL
```
http://[servername]:[port]/newrelic/rest/api/metrics/get
```
Upon successful startup the response would be a list of all the metrics that are being monitored in the system.

## Uninstall 
-------------
Remove OE.Newrelic from the build and startup arguments to quickly remove the module from your application

