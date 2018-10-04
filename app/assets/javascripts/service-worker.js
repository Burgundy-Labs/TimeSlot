/*
Copyright 2016 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

(function() {
    'use strict';

    const filesToCache = [
        '/.',
        '/assets/stylesheets/main.css',
        '/assets/lib/bootstrap/css/bootstrap.min.css',
        '/assets/lib/bootstrap/js/bootstrap.min.js',
        '/assets/images/Logo.svg',
        '/assets/images/TimeSlot_Dark.svg',
    ];

    const staticCacheName = 'pages-cache-v2';

    self.addEventListener('install', function(event) {
        event.waitUntil(
            caches.open(staticCacheName)
                .then(function(cache) {
                    return cache.addAll(filesToCache);
                })
        );
    });

    self.addEventListener('fetch', function(event) {
        event.respondWith(
            caches.match(event.request).then(function(response) {
                if (response) {
                    console.log('Found ', event.request.url, ' in cache');
                    return response;
                }
                console.log('Network request for ', event.request.url);
                return fetch(event.request).then(function(response) {
                    return caches.open(staticCacheName).then(function(cache) {
                        if (event.request.url.indexOf('test') < 0) {
                            cache.put(event.request.url, response.clone());
                        }
                        return response;
                    });
                });
            }).catch(function(error) {
                console.log('Error, ', error);
            })
        );
    });

    self.addEventListener('activate', function(event) {
        const cacheWhitelist = [staticCacheName];
        event.waitUntil(
            caches.keys().then(function(cacheNames) {
                return Promise.all(
                    cacheNames.map(function(cacheName) {
                        if (cacheWhitelist.indexOf(cacheName) === -1) {
                            return caches.delete(cacheName);
                        }
                    })
                );
            })
        );
    });

})();