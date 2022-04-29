# WeblectricFence
Weblectric Fence (read web electric fence) is a web security library that is based on Apache Shiro. This is not a fork of that project, but
follows some of the design. It is apparent that lot of people have trouble getting Shiro working with Spring Boot web application. This project
tries to address the issues and aims for easy integration with Spring Boot
Web application.

This is a Spring project and includes Spring Boot starter component for easier usage.

Controller methods that requires specific permission must be annotated with RequiresPermission. If the method is not annotated it does not
require any annotation, but requires the requester to be authenticated. This behaviour can be omitted by addin the path to ExcludeFilter
which contains the paths not requiring authentication, /login for example.
