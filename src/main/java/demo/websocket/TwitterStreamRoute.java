package demo.websocket; 

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.TwitterComponent;
import org.apache.camel.component.websocket.WebsocketComponent;

public class TwitterStreamRoute extends RouteBuilder{

        //put your twitter keys here to test 
        public final String CONSUMER_KEY = "";
	public final String CONSUMER_SECRET = "";
	public final String ACCESS_TOKEN = "";
	public final String ACCESS_TOKEN_SECRET = "";


        @Override
	public void configure() throws Exception {


		
/*        WebsocketComponent wc = getContext().getComponent("websocket", WebsocketComponent.class);
        wc.setPort(9292);
        wc.setStaticResources("classpath:.");*/

        TwitterComponent tc = getContext().getComponent("twitter", TwitterComponent.class);
        tc.setAccessToken(ACCESS_TOKEN);
        tc.setAccessTokenSecret(ACCESS_TOKEN_SECRET);
        tc.setConsumerKey(CONSUMER_KEY);
        tc.setConsumerSecret(CONSUMER_SECRET);
        
        fromF("twitter://streaming/filter?type=polling&delay=%s&keywords=%s", "5", "pittsburgh")
		.process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					String res = exchange.getIn().getBody().toString();
					exchange.getOut().setBody(res);
				}
			})
        .to("websocket://0.0.0.0:9292/camel-tweet?sendToAll=true");
	}

}
