{
    "name" : "Kunsthandwerk",
    "source" : {
        "url" : "http://www.jungekunst-magazin.de/kunsthandwerk_design/einzelhefte.asp"
    },
    "elements" : [
        {
            "selectorAll" : "body table table tbody td.meldungen p",
            "validate" : {
                "subSelector" : "",
                "regex" : ".*Nr. [1-4].*"
            },
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : "p",
                            "modify" : function(result) {
                                return result.match("Nr. [1-4]/([0-9]{4})")[1];
                            }
                        },
                        "month" : {
                            "subSelector" : "p",
                            "modify" : function(result) {
                                var i = parseInt(result.match("Nr. ([1-4])/[0-9]{4}")[1]);
                                return ""+Math.round(12 / 4 * i);
                            }
                        }
                    },
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : "p > a"
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "table table table > tbody:eq(0)",
                                    "tasks" : [
                                        {
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : "span.standard > font",
                                                    "modify" : function(result) {
                                                        return result + ".";
                                                    }
                                                }
                                            ],
                                            "follow" : [
                                                {
                                                    "url" : {
                                                        "subSelectorAll" : "tr > td > a"
                                                    },
                                                    "elements" : [
                                                        {
                                                            "selectorAll" : "table p:eq(3)",
                                                            "tasks" : [
                                                                {
                                                                    "parse" : [
                                                                        {
                                                                            "subSelectorAll" : "span",
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
                    ]
                }
            ]
        }
    ]
}