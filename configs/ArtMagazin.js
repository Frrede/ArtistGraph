{
    "name" : "Art Magazin",
    "source" : {
        "url" : "http://www.art-magazin.de/div/heftarchiv/$y$/$m$",
        "loops" : {
            "y" : {
                "from" : 1979,
                "to" : 2013
            },
            "m" : {
                "from" : 1,
                "to" : 12
            }
        }
    },
    "elements" : [
        {
            "selectorAll" : ".ergebnisbox > .contentRoundShadow",
            "globalDate" : {
                "year" : {
                    "selector" : "#leftcolumn div.ueberschrift",
                    "modify" : function(result) {
                        return result.match(/Heftarchiv - Ausgabe: [0-9]+ \/ ([0-9]+)/)[1];
                    }
                },
                "month" : {
                    "selector" : "#leftcolumn div.ueberschrift",
                    "modify" : function(result) {
                        return result.match(/Heftarchiv - Ausgabe: ([0-9]+) \/ [0-9]+/)[1];
                    }
                }
            },
            "tasks" : [
                {
                    "parse" : [
                        {
                            "subSelectorAll" : "h1",
                            "modify" : function(result) {
                                return result + ".";
                            }
                        },
                        {
                            "subSelectorAll" : "p",
                            "modify" : function(result) {
                                return result;
                            }
                        }
                    ]
                    
                }
            ]
            
            
            
        }
    ]
    
}