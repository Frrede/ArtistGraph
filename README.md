Artist Graph
============

This project provides a system for the extration of artist names in art related media.<br/>
The extraction process is configurable with a special JSON/Javascript config file and based on a database with artist namens (artistnames.sql).<br/>
The result is saved in the database as well (namedata.sql).<br/>
The databases are configurable in settings.ini.


Example of a config file
------------------------

 ```json
{
    "name" : "Medien-Name",
    "source" : {
        "url" : "http://www.page.com/archiv?jahr=$a$&monat=$b$&page=$c$",
        "loops" : {
            "a" : {
                "from" : 1900,
                "to" : 2013
            },
            "b" : {
                "from" : 1,
                "to" : 12
            },
            "c" : {
                "from" : 1,
                "to" : 5
            }
        }
    },
    "elements" : [
        {
            "selectorAll" : "",
            "validate" : {
                "subSelector" : "",
                "regex" : ""
            },
            "globalDate" : {
                "year" : {
                    "selector" : "",
                    "modify" : function(result) {
                        return result;
                    }
                },
                "month" : {
                    "selector" : "",
                    "modify" : function(result) {
                        return result;
                    }
                },
            },
            "tasks" : [
                {
                    "date" : {
                        "year" : {
                            "subSelector" : "",
                            "modify" : function(result) {
                                return result;
                            }
                        },
                        "month" : {
                            "subSelector" : "",
                            "modify" : function(result) {
                                return result;
                            }
                        },
                    },
                    "parse" : [
                        {
                            "subSelectorAll" : "",
                            "modify" : function(result) {
                                return result;
                            }
                        }
                    ],
                    "follow" : [
                        {
                            "url" : {
                                "subSelectorAll" : "",
                                "modify" : function(result) {
                                    return result;
                                }
                            },
                            "elements" : [...]
                        }
                    ]
                }
            ]
        }
    ]
}
 ```

Required libraries
------------------
Stanford NER - http://nlp.stanford.edu/downloads/CRF-NER.shtml<br/>
jsoup - http://jsoup.org


License
-------

Apache License Version 2.0
http://apache.org/licenses/LICENSE-2.0.txt
