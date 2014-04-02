{
    "hosts" : [{
    "host" : "mon-site.com",
    "lb" : "roundrobin",
    "workers" : [
        '10.66.1.2',
        '10.66.1.3:45678'
                ]
    "headers" : {
        "incoming": {
            "add" : [{"My-ProxyHeader" : "123"}]
                    },

        "outgoing" : {
            "add" : [{"For-Client-Header" : "765"}]
                     }
                }
            }]
}