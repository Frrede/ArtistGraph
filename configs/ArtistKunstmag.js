{
    "name" : "Artist Kunstmagazin",
    "source" : {
        "url" : "http://www.artist-kunstmagazin.de/?show=ausgabe&id=$i$",
        "loops" : {
            "i" : {
                "from" : 19,
                "to" : 112
            }
        }
    },
    "elements" : [
        {
            "selectorAll" : "#textbox",
            "validate" : {
                "subSelector" : "h1",
                "regex" : ".*Heft [0-9]+.*"
            },
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : "h1",
                            "modify" : function(result) {
                                var nr = parseInt(result.match("Heft ([0-9]+)")[1]);
                                
                                if(nr < 0 || nr > 96) return "-1";
                                
                                var years = [null, 
                                    1989, 1989,
                                    1990, 1990, 1990, 1990, null, 
                                    1991, 1991, 1991, null, 
                                    1992, 1992, 1992, null, 
                                    1993, 1993, null, 
                                    1994, 1994, 1994, 
                                    1995, 1995, 1995, 1995, 
                                    1996, 1996, 1996, 1996, 
                                    1997, 1997, 1997, 1997, 
                                    1998, 1998, 1998, 1998, 
                                    1999, 1999, 1999, 1999, 
                                    2000, 2000, 2000, 2000, 
                                    2001, 2001, 2001, 2001,
                                    2002, 2002, 2002, 2002,
                                    2003, 2003, 2003, 2003,
                                    2004, 2004, 2004, 2004,
                                    2005, 2005, 2005, 2005,
                                    2006, 2006, 2006, 2006,
                                    2007, 2007, 2007, 2007,
                                    2008, 2008, 2008, 2008,
                                    2009, 2009, 2009, 2009,
                                    2010, 2010, 2010, 2010,
                                    2011, 2011, 2011, 2011,
                                    2012, 2012, 2012, 2012,
                                    2013, 2013, 2013
                                ];
                                
                                return ""+years[nr];
                            }
                        },
                        "month" : {
                            "subSelector" : "h1",
                            "modify" : function(result) {
                                var nr = parseInt(result.match("Heft ([0-9]+)")[1]);
                                
                                if(nr < 0 || nr > 96) return "-1";
                                
                                var nrs = [null, 
                                    3, 4,
                                    1, 2, 3, 4, null, 
                                    2, 3, 4, null, 
                                    2, 3, 4, null, 
                                    2, 3, null, 
                                    1, 2, 3, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4,  
                                    1, 2, 3, 4,  
                                    1, 2, 3, 4,  
                                    1, 2, 3, 4,  
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3, 4, 
                                    1, 2, 3
                                ];
                                
                                return ""+Math.round(12 / 4 * nrs[nr]);
                            }
                        }
                    },
                    "parse" : [
                        {
                            "subSelectorAll" : "td.lineheight a",
                            "modify" : function(result) {
                                return result+"."; 
                            }
                        }
                    ],
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : "td.lineheight a"
                            },
                            "elements" : [
                                {
                                    "selectorAll" : "#textbox",
                                    "tasks" : [
                                        {
                                            "parse" : [
                                                {
                                                    "subSelectorAll" : "div > p"
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