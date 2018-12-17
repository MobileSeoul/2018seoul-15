<template>
    <v-app>
        <v-toolbar app color="#6987f7">
            <v-layout row wrap>
                <v-flex xs6 style="text-align:left;">
                    <v-btn icon flat @click="$router.go(-1)">
                        <v-icon large color="white">arrow_back</v-icon>
                    </v-btn>
                </v-flex>
                <v-flex xs6 style="text-align:right; padding-top: 8px; padding-right: 10px;">
                    <h1 style="color: white;">서비스 상세</h1>
                </v-flex>
            </v-layout>
        </v-toolbar>

        <v-content>
            <v-container v-if="isLoading" fill-height>
                <v-layout style="text-align: center; padding-top: 200px;">
                    <v-flex 12><v-progress-circular indeterminate color="primary" style="margin: auto;"></v-progress-circular></v-flex>
                </v-layout>
            </v-container>
            <v-container v-else style="padding:0;">
                <v-layout>
                    <v-flex xs12>
                        <v-img :src="service_api['IMG_PATH']">
                            <v-layout
                                    pa-3
                                    column
                                    fill-height
                                    class="white--text"
                                    style="background-image: linear-gradient(to top, rgba(0, 0, 0, 0.9) 0%, transparent 140px);"

                            >
                                <v-spacer></v-spacer>
                                <v-flex shrink>
                                    <div class="body-1">{{ service_api['AREANM'] }}</div>

                                    <v-layout align-end justify-space-between row fill-height>
                                        <div class="subheading"
                                        >{{ service_api['SVCNM'] }}</div>
                                        <v-btn icon style="margin:0;"
                                               @click="toggleBookmark(service_api['SVCID'])">
                                            <v-icon color="#ff6161" v-if="isBookmarked">favorite</v-icon>
                                            <v-icon color="#f5f5f5" v-else>favorite_border</v-icon>
                                        </v-btn>
                                    </v-layout>
                                </v-flex>
                            </v-layout>
                        </v-img>
                        <!--
                        <v-jumbotron
                                :gradient="gradient"
                                :src="service_api['IMG_PATH']"
                        >
                            <v-container fill-height style="padding:0;">
                                <v-layout align-center>
                                    <v-flex text-xs-center>
                                        <h3 style="color:white;">{{ service_api['SVCNM'] }}</h3>
                                    </v-flex>
                                </v-layout>
                            </v-container>
                        </v-jumbotron>
                        -->
                    </v-flex>
                </v-layout>
            </v-container>

            <v-container style="padding:0;">
                <!-- 주소정보 레코드 -->
                <v-layout>
                    <v-flex xs12 style="background-color:#f9f9f9; border: 1px solid #f1f1f1; border-bottom: 0;">
                        <v-list dense class="pt-0">
                            <v-list-tile>
                                <v-list-tile-content>
                                    <v-list-tile-title>{{ service_api['ADRES'] }} ({{ service_api['PLACENM'] }})</v-list-tile-title>
                                </v-list-tile-content>
                                <v-list-tile-action>
                                    <v-icon style="color: #ff6161;">place</v-icon>
                                </v-list-tile-action>
                            </v-list-tile>
                        </v-list>
                    </v-flex>
                </v-layout>

                <!-- 기간정보 레코드 -->
                <v-layout>
                    <v-flex xs12 style="background-color:#f9f9f9; border: 1px solid #f1f1f1; border-bottom: 0;">
                        <v-list dense class="pt-0">
                            <v-list-tile>
                                <v-list-tile-content>
                                    <v-list-tile-title>{{ service_api['SVCOPNBGNDT'] }} ~ {{ service_api['SVCOPNENDDT'] }} ( {{service_api['V_MIN']}} ~ {{service_api['V_MAX']}} )</v-list-tile-title>
                                </v-list-tile-content>
                                <v-list-tile-action>
                                    <v-icon style="color: #6361ff;">calendar_today</v-icon>
                                </v-list-tile-action>
                            </v-list-tile>
                        </v-list>
                    </v-flex>
                </v-layout>
                <v-layout>
                    <!-- 전화번호 레코드 -->
                    <v-flex xs6 style="background-color:#f9f9f9; border: 1px solid #f1f1f1; border-bottom: 0; border-right: 0;">
                        <v-list dense class="pt-0">
                            <v-list-tile>
                                <v-list-tile-content>
                                    <v-list-tile-title>{{ service_api['SVCENDTELNO'] }}</v-list-tile-title>
                                </v-list-tile-content>
                                <v-list-tile-action>
                                    <v-icon color="#5c8dfc">smartphone</v-icon>
                                </v-list-tile-action>
                            </v-list-tile>
                        </v-list>
                    </v-flex>
                    <!-- 요금정보 레코드 -->
                    <v-flex xs6 style="background-color:#f9f9f9; border: 1px solid #f1f1f1; border-bottom: 0;">
                        <v-list dense class="pt-0">
                            <v-list-tile>
                                <v-list-tile-content>
                                    <v-list-tile-title>{{ service_api['PAYAT'] }}</v-list-tile-title>
                                </v-list-tile-content>
                                <v-list-tile-action>
                                    <v-icon style="color: #ffc72a;">monetization_on</v-icon>
                                </v-list-tile-action>
                            </v-list-tile>
                        </v-list>
                    </v-flex>
                </v-layout>
            </v-container>

            <!-- 네이버 지도 -->
            <v-container style="padding:0;">
                <v-layout>
                    <v-flex xs12>
                        <div id="map" style="width:100%;height:250px;">
                            <p v-if="!isMapDataExsist" class="text-xs-center" style="vertical-align: middle; line-height: 250px; background-color: #eee;">지도 정보가 없습니다</p>
                        </div>
                    </v-flex>
                </v-layout>
            </v-container>

            <v-container style="padding:0;">
                <v-layout>
                    <v-flex xs12>
                        <v-expansion-panel value="1">
                            <v-expansion-panel-content>
                                <div slot="header">상세설명</div>
                                <v-card>
                                    <v-card-text
                                            v-html="service_api['DTLCONT']"
                                            class="proto-description"
                                    ></v-card-text>
                                </v-card>
                            </v-expansion-panel-content>
                            <v-expansion-panel-content>
                                <div slot="header">주의사항</div>
                                <v-card>
                                    <v-card-text
                                            v-html="service_api['NOTICE']"
                                            class="proto-description"
                                    ></v-card-text>
                                </v-card>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-flex>
                </v-layout>
            </v-container>

            <v-container style="padding:0;">
                <v-layout>
                    <v-flex xs6>
                        <v-btn block flat depressed style="margin:0; height: 48px; background-color: #ddd; border-radius: 0;">
                            <v-icon small>call</v-icon>전화하기
                        </v-btn>
                    </v-flex>
                    <v-flex xs6>
                        <a target="_blank" :href="service['SVCURL']" style="text-decoration: none;">
                            <v-btn block flat dark depressed style="margin:0; height: 48px; background-color: #6987f7; border-radius: 0;">
                                <v-icon small>bookmark</v-icon>예약하기
                            </v-btn>
                        </a>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    export default {
        name: "Service",
        data: function() {
            return {
                service: [],
                service_api: [],
                gradient: 'to top right, rgba(0, 0, 0, 0.7), rgba( 0, 0, 0, 0.7)',
                isLoading: true,
                isMapDataExsist: true,
                isBookmarked: false,
            }
        },
        created: function() {
            },
        mounted: function() {

            var self = this;

            axios.post('/service', { SVCID: this.$store.state.service })
                .then(function (response) {

                    self.service = response.data;

                    console.log(self.service);

                    var API_URL = "http://openAPI.seoul.go.kr:8088/414f414a6963686f33316d65547677/json/ListPublicReservationDetail/1/1/";

                    self.service['SVCURL'] = "https://yeyak.seoul.go.kr/mobile/detailView.web?rsvsvcid=" + self.service['SVCID'];

                    axios.get(API_URL + self.service['SVCID'])
                        .then(function (response) {
                            self.service_api = response.data.ListPublicReservationDetail.row[0];

                            window.seoulApp.toggleBookmark(self.service['SVCID'], false);
                            window.seoulApp.toggleBookmark(self.service['SVCID'], false);

                            console.log(self.service_api)

                            self.service_api['SVCOPNBGNDT'] = self.service_api['SVCOPNBGNDT'].replace("00:00:00.0", "");
                            self.service_api['SVCOPNENDDT'] = self.service_api['SVCOPNENDDT'].replace("00:00:00.0", "");

                            self.service_api['DTLCONT'] = self.service_api['DTLCONT'].replace("<pre>", "");
                            self.service_api['DTLCONT'] = self.service_api['DTLCONT'].replace("</pre>", "");

                            if( self.service_api['X'] === '' ) {
                                naver.maps.Service.geocode({
                                    address: self.service_api['ADRES']
                                }, function(status, response) {
                                    if (status !== naver.maps.Service.Status.OK) {
                                        self.isMapDataExsist = false;
                                        self.isLoading = false;
                                        return;
                                    }

                                    var result = response.result, // 검색 결과의 컨테이너
                                        item = result.items[0]; // 검색 결과의 배열

                                    self.service_api['X'] = item['point']['y'];
                                    self.service_api['Y'] = item['point']['x'];

                                    var mapOptions = {
                                        center: new naver.maps.LatLng( self.service_api['X'] , self.service_api['Y']),
                                        zoom: 10
                                    };

                                    var map = new naver.maps.Map('map', mapOptions);

                                    var marker = new naver.maps.Marker({
                                        position: new naver.maps.LatLng(self.service_api['X'], self.service_api['Y']),
                                        map: map
                                    });
                                    self.isLoading = false;


                                });
                            } else {
                                var mapOptions = {
                                    center: new naver.maps.LatLng( self.service_api['X'] , self.service_api['Y']),
                                    zoom: 10
                                };

                                var map = new naver.maps.Map('map', mapOptions);

                                var marker = new naver.maps.Marker({
                                    position: new naver.maps.LatLng(self.service_api['X'], self.service_api['Y']),
                                    map: map
                                });

                                self.isLoading = false;
                            }
                        })
                        .catch(function (error){
                            console.log(error);
                        });

                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        methods: {
            toggleBookmark : function(SVCID) {
                window.seoulApp.toggleBookmark(SVCID, true);
            },
            setIsBookmarked : function(is) {
                console.log(is);
                this.isBookmarked = is;
            }
        }
    }
</script>

<style scoped>
    .proto-description * {
        background-color: white !important;
    }
    .proto-description strong {
        background-color: white !important;
    }
</style>