{
    "name" : "Texte zur Kunst",
    "source" : {
        "url" : "http://www.textezurkunst.de/archiv/",
    },
    "elements" : [
        {
            "selectorAll" : ".issues-list > .title",
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : "a",
                            "modify" : function(result) {
                                return result.match("/ [a-zA-ZäüöÄÜÖ]+ ([0-9]{4})")[1];
                            }
                        },
                        "month" : {
                            "subSelector" : "a",
                            "modify" : function(result) {
                                var m = result.match("/ ([a-zA-ZäüöÄÜÖ]+) [0-9]{4}")[1];
                                switch (m.toLowerCase()) {
                                    case "januar":
                                        return "1";
                                    case "februar":
                                        return "2";
                                    case "märz":
                                        return "3";
                                    case "april":
                                        return "4";
                                    case "mai":
                                        return "5";
                                    case "juni":
                                        return "6";
                                    case "juli":
                                        return "7";
                                    case "august":
                                        return "8";
                                    case "september":
                                        return "9";
                                    case "oktober":
                                        return "10";
                                    case "november":
                                        return "11";
                                    case "dezember":
                                        return "12";
                                }
                            }
                        }
                    },
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : "a"
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "#content",
                                    "tasks" : [
                                        {
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : "span.title",
                                                    "modify" : function(result) {
                                                        return result + ".";
                                                    }
                                                },
                                                {
                                                    "subSelectorAll" : "span.subtitle",
                                                    "modify" : function(result) {
                                                        return result + ".";
                                                    }
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