{
    "name" : "west.art",
    "source" : {
        "url" : "http://www.wdr.de/tv/west-art/archiv/meisterwerke_$y$.phtml",
        "loops" : {
            "y" : {
                "from" : 2006,
                "to" : 2009
            }
        }
    },
    "elements" : [
        {
            "selectorAll" : "td.kleineBox",
            "validate" : {
                "subSelector" : "a.fliesstextlink",
                "regex" : ".*3_pfeile_ani_4.gif.*"
            },
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : "",
                            "modify" : function(result) {
                                return result.match("[0-9]+. [a-zA-ZäüöÄÜÖ]+ ([0-9]{4})")[1];
                            }
                        },
                        "month" : {
                            "subSelector" : "",
                            "modify" : function(result) {
                                var m = result.match("[0-9]+. ([a-zA-ZäüöÄÜÖ]+) [0-9]{4}")[1];
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
                                "subSelectorAll" : "a.fliesstextlink"
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "body",
                                    "tasks" : [
                                        {
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : "span.fliesstextblok"
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