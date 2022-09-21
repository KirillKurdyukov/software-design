package ru.itmo.nasa.api

val listIntervalSolarFlaresBody = """
                                    [
                                      {
                                        "flrID": "2022-09-04T07:35:00-FLR-001",
                                        "instruments": [
                                          {
                                            "displayName": "GOES-P: EXIS 1.0-8.0"
                                          }
                                        ],
                                        "beginTime": "2022-09-04T07:35Z",
                                        "peakTime": "2022-09-04T07:54Z",
                                        "endTime": "2022-09-04T08:12Z",
                                        "classType": "C3.9",
                                        "sourceLocation": "S27E25",
                                        "activeRegionNum": 13093,
                                        "linkedEvents": [
                                          {
                                            "activityID": "2022-09-04T08:48:00-CME-001"
                                          }
                                        ],
                                        "link": "https://kauai.ccmc.gsfc.nasa.gov/DONKI/view/FLR/21529/-1"
                                      },
                                      {
                                        "flrID": "2022-09-05T17:58:00-FLR-001",
                                        "instruments": [
                                          {
                                            "displayName": "GOES-P: EXIS 1.0-8.0"
                                          }
                                        ],
                                        "beginTime": "2022-09-05T17:58Z",
                                        "peakTime": "2022-09-05T18:05Z",
                                        "endTime": "2022-09-05T18:14Z",
                                        "classType": "M1.0",
                                        "sourceLocation": "S23W85",
                                        "activeRegionNum": 13089,
                                        "linkedEvents": null,
                                        "link": "https://kauai.ccmc.gsfc.nasa.gov/DONKI/view/FLR/21549/-1"
                                      },
                                      {
                                        "flrID": "2022-09-17T12:42:00-FLR-001",
                                        "instruments": [
                                          {
                                            "displayName": "GOES-P: EXIS 1.0-8.0"
                                          }
                                        ],
                                        "beginTime": "2022-09-17T12:42Z",
                                        "peakTime": "2022-09-17T13:19Z",
                                        "endTime": "2022-09-17T13:21Z",
                                        "classType": "M1.0",
                                        "sourceLocation": "N18W98",
                                        "activeRegionNum": 13098,
                                        "linkedEvents": [
                                          {
                                            "activityID": "2022-09-17T14:00:00-CME-001"
                                          }
                                        ],
                                        "link": "https://kauai.ccmc.gsfc.nasa.gov/DONKI/view/FLR/21649/-1"
                                      }
                                    ]
                                    """.trimIndent()

val solarFlareBody = """
                        [
                          {
                            "flrID": "2022-09-21T06:51:00-FLR-001",
                            "instruments": [
                              {
                                "displayName": "GOES-P: EXIS 1.0-8.0"
                              }
                            ],
                            "beginTime": "2022-09-21T06:51Z",
                            "peakTime": "2022-09-21T07:02Z",
                            "endTime": "2022-09-21T07:18Z",
                            "classType": "M1.0",
                            "sourceLocation": "S28E73",
                            "activeRegionNum": 13107,
                            "linkedEvents": null,
                            "link": "https://kauai.ccmc.gsfc.nasa.gov/DONKI/view/FLR/21693/-1"
                          }
                        ]
                    """.trimIndent()