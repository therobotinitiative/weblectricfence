## WeblectricFence
Weblectric Fence (read web electric fence) is a web security library that is based on Apache Shiro. This is not a fork of that project, but
follows some of the design. It is apparent that lot of people have trouble getting Shiro working with Spring Boot web application. This project
tries to address the issues and aims for easy integration with Spring Boot
Web application.

This is a Spring project and includes Spring Boot starter component for easier usage.

Controller methods that requires specific permission must be annotated with RequiresPermission. If the method is not annotated it does not
require any annotation, but requires the requester to be authenticated. This behaviour can be omitted by addin the path to ExcludeFilter
which contains the paths not requiring authentication, /login for example.

# Setup

# FenceConfig

The implementing bean will give applicationspecific configuration

# AuthorizationMatcher

Responsible for associating URI to permission.

# AuthenticationWorker and Authenticator

AuthenticationWorker handles all the authentication related actions required by the library, but the actual authentication is delegated into Authenticator
bean which MUST be provided by the application.

## AuthorizationMatcher
The authorization filter uses this interface to determine if the requested path needs a specific permission. Implementing bean determines how the path is matched.
DefaultAuthorizationMatcher is provided which uses ANT styled path matching. Application can provide application specific implementation or use the default
implementation. This bean is required. Authorization is performed only if the path is under the specified secure context root.

## FilterExceptionHandler

FilterExceptionHandler can be used to capture filter level exceptions and handle them accordingly. If the bean is not present the exception is handled as normally.
This gives the ability to create centralized exception handler in the filter level.

### AbstractFilterExceptionHandler