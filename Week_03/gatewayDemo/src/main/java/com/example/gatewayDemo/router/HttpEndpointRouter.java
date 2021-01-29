package com.example.gatewayDemo.router;

import java.util.List;

public interface HttpEndpointRouter {
    String route(List<String> endpoints);
}
