{
    "name" : "Photo International",
    "source" : {
        "url" : "http://www.photo-international.de/archiv.html"
    },
    "elements" : [
        {
            "selectorAll" : "#txt",
            "tasks" : [
                {
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : "a.s9b"
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "body",
                                    "tasks" : [
                                        {
                                            "date" : {
                                                "year" : {
                                                    "subSelector" : "#wegweiser span.headln",
                                                    "modify" : function(result) {
                                                        return result.match("[1-6]{1} \\| ([0-9]{4})")[1];
                                                    }
                                                },
                                                "month" : {
                                                    "subSelector" : "#wegweiser span.headln",
                                                    "modify" : function(result) {
                                                        var i = result.match("([1-6]{1}) \\| [0-9]{4}")[1];
                                                        return ""+Math.round(12 / 6 * i);
                                                    }
                                                }
                                            },
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : "#txt tr td.s9"
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
}