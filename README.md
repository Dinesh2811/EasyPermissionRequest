# EasyPermissionRequest Library

#### Welcome to the EasyPermissionRequest library!

EasyPermissionRequest is a user-friendly Android open-source library that simplifies the process of requesting runtime permissions in your Android applications. With this library, you can easily integrate runtime permission handling without the hassle of writing boilerplate code.

## Getting Started
To integrate EasyPermissionRequest into your project, follow these simple steps:
- ##### Add the JitPack repository to your gradle (kts) file
  >
        repositories {
            ...
            maven ("https://jitpack.io")
        }
- ##### Add the dependency to your build.gradle {kts)
  >
        dependencies {
            ...
            implementation("com.github.Dinesh2811:EasyPermissionRequest:1.1")
        }
- ##### Request Permissions
  >
        val permissionRequestBuilder = EasyPermissionRequestBuilder(this)
        val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA)

        permissionRequestBuilder.setPermissionCallback(object : PermissionCallback {
            override fun onPermissionCallback(granted: List<String>, denied: List<String>) {
                //  TODO("Implement your code here")
            }
        }).requestPermission(permissionsToRequest)
- ##### Request Permissions with detailed information (Experimental)
  >
        val permissionRequestBuilder = EasyPermissionRequestBuilder(this)
        val permissionsToRequest = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA)

        permissionRequestBuilder
            .setLogging(true)
            .repeatRequestPermission(true)
            .setPermissionCallback(object : PermissionCallbackExperimental{
                override fun onPermissionGranted(granted: List<String>) {
                    //  TODO("Implement your code for granted permission here")
                }

                override fun onPermissionDenied(denied: List<String>, permanentlyDenied: List<String>, cancelled: List<String>) {
                    //  TODO("Implement your code here")
                }
            })
            .requestPermission(permissionsToRequest)


## Contribution
Whether you're looking to offer your expertise, suggest improvements, report issues, or ask questions, your involvement is highly valued and much appreciated. Any kind of contributions from the community are most welcomed.

#### Here's how you can contribute:

- #### Report Issues:
  If you come across any bugs, glitches, or unexpected behavior, please report them using the GitHub issue tracker. Be sure to provide detailed steps to reproduce the issue and any relevant information.

- #### Submit Pull Requests:
  Have a fix or enhancement in mind? You can submit pull requests with your proposed changes. We'll review them and work together to integrate valuable additions into the library.

- #### Share Ideas:
  Have ideas for new features or improvements? Feel free to open discussions in the GitHub repository. Your input can help shape the future of EasyPermissionRequest.


## Getting Help
If you have questions about using the library, implementing specific features, or understanding the code, don't hesitate to reach out. Use the GitHub issue tracker to ask questions or seek clarifications.

## Spread the Word
If you find EasyPermissionRequest useful, consider sharing it with fellow developers and communities. Let's make permission handling easier for everyone!

## License

Licensed under the terms of the [Apache License 2.0][7]. See [License](LICENSE) for details.

```
   Copyright 2023 Dinesh K

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

[7]: https://www.apache.org/licenses/LICENSE-2.0

# Happy coding!
