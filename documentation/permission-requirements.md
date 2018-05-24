# Permission Requirements

## Permissions
This app needs the following permissions:

* [ACCESS_NETWORK_STATE][]: Allows applications to access information about networks. This permission is optional and is used for the update function (see below).
* [INTERNET][]: Allows applications to open network sockets. This permission is optional and is used for the update function (see below).

## Dependent functions

### Update function
The update function checks for new versions of the app online. This function can be set to run on every start of the app (default off). In case the necessary permissions ([ACCESS_NETWORK_STATE][], [INTERNET][]) are not granted the update function will fail. Other functions are not affected. The update function does not download the new app.

[ACCESS_NETWORK_STATE]: https://developer.android.com/reference/android/Manifest.permission#access_network_state
[INTERNET]: https://developer.android.com/reference/android/Manifest.permission#internet