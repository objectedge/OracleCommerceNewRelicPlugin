package com.ws.core;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import atg.nucleus.Nucleus;
import atg.nucleus.naming.ComponentName;

import com.objectedge.newrelic.collector.MetricsCollectorTools;
import com.objectedge.newrelic.json.MetricsResponseJSON;

/**
 * The class registers its methods for the HTTP GET request using the @GET
 * annotation. Using the @Produces annotation, it defines that it can deliver
 * several MIME types e.g. text, XML and HTML.
 * 
 * @author Chandan Kushwaha
 * 
 */

// Sets the path to base URL + /api/metrics
@Path("/api/metrics")
public class MetricsResource {

	// pre-parsed component name
	private static ComponentName CMP_METRICS_COLL_TOOLS = ComponentName
			.getComponentName("/oe/newrelic/plugin/collector/MetricsCollectorTools");

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "application/json" media type.
	 * 
	 * @return MetricsResponseJSON object that will be returned as a
	 *         application/json response.
	 */
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public MetricsResponseJSON getMetrics() {
		// initialize response object
		MetricsResponseJSON response = new MetricsResponseJSON();
		// resolve component
		Object object = Nucleus.getGlobalNucleus().resolveName(
				CMP_METRICS_COLL_TOOLS);
		// if component is not null then get metric response using component
		if (object != null) {
			MetricsCollectorTools collectorTools = (MetricsCollectorTools) object;
			response = collectorTools.getMetrics();
		}

		return response;
	}

	// TODO: handle response and send correct status code and message
	public Response handleResponse(MetricsResponseJSON response) {
		return null;
	}
}
