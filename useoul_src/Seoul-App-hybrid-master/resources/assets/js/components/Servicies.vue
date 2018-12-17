<template>
    <v-app>
        <v-toolbar app style="background-image: linear-gradient(to left,#ff63a6,#6e8af8 50%,#11d9c3 99%);">
            <v-layout row wrap>
                <v-flex xs6 style="text-align:left;">
                    <router-link to="/" style="text-decoration: none;">
                        <v-btn icon flat>
                            <v-icon large color="white">arrow_back</v-icon>
                        </v-btn>
                    </router-link>
                </v-flex>
                <v-flex xs6 style="text-align:right; padding-top: 8px; padding-right: 10px;">
                    <h1 style="color: white;">{{ computedCategoryName }}</h1>
                </v-flex>
            </v-layout>
        </v-toolbar>
        <v-content>
            <v-container style="padding:0;">
                <v-layout wrap>
                    <v-flex xs12>
                        <v-carousel class="elevation-0" hide-delimiters style="height: 300px; background-color: #eee;-">
                            <v-carousel-item
                                    v-for="(item, i) in recommend"
                                    :key="i"
                                    :src="item['IMG_URL']"
                                    @click="goToDetail( item['SVCID'] )"
                            >
                                <v-layout
                                        pa-3
                                        column
                                        fill-height
                                        class="white--text"
                                        style="background-image: linear-gradient(to top, rgba(0, 0, 0, 0.9) 0%, transparent 140px);"
                                >
                                    <v-spacer></v-spacer>
                                    <v-flex shrink>
                                        <div class="body-1">{{ item['AREANM'] }}</div>
                                        <div class="subheading">{{ item['SVCNM'] }}</div>
                                    </v-flex>
                                </v-layout>
                            </v-carousel-item>
                        </v-carousel>
                    </v-flex>
                </v-layout>
            </v-container>

            <v-container style="background-color: #fff;">
                <v-layout wrap align-center>
                    <v-flex xs12>
                        <v-select
                             :items="places"
                             label="- 지역명 -"
                             color="#598bf9"
                             background-color="#aaa"
                             outline
                             v-model="AREANM"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 style="margin-top: -20px;">
                        <v-select
                            :items="computedMinclasses"
                            label="- 소분류 -"
                            color="#598bf9"
                            background-color="#aaa"
                            outline
                            v-model="MINCLASSNM"
                        ></v-select>
                    </v-flex>
                    <v-flex xs12 style="margin-top: -20px;">
                        <v-btn
                            block
                            outline
                            large
                            color="#598bf9"
                            @click="getSearchedData"
                        >
                            검색하기
                        </v-btn>
                    </v-flex>
                </v-layout>
            </v-container>
            <v-divider></v-divider>
            <v-container fluid style="padding: 0;">
                <v-layout>
                    <v-flex xs12>
                        <v-list two-line style="padding-bottom: 0;">
                            <template
                                    v-for="(item, index) in ServicesData"
                                    >
                                <v-list-tile
                                        :key="item.SVCID"
                                        @click="goToDetail( item['SVCID'] )"
                                        avatar
                                >
                                    <v-list-tile-avatar>
                                        <img :src="item['IMG_URL']">
                                    </v-list-tile-avatar>

                                    <v-list-tile-content>
                                        <v-list-tile-title>{{ item['SVCNM'] }}</v-list-tile-title>
                                        <v-list-tile-sub-title>({{ item['AREANM'] }}) {{ item['PLACENM'] }}</v-list-tile-sub-title>
                                    </v-list-tile-content>
                                </v-list-tile>
                                <v-divider></v-divider>
                            </template>
                        </v-list>
                    </v-flex>
                </v-layout>
            </v-container>

            <v-container style="background-color: #fff; padding:0;">
                <v-layout>
                    <v-flex xs12 flat style="">
                        <v-btn
                                v-if="isGetMore == 0"
                                style="margin: 0; height: 44px;"
                                depressed
                                block
                                @click="getMoreData">
                            더보기
                        </v-btn>
                        <v-btn
                                v-else-if="isGetMore == 1"
                                style="margin: 0; height: 44px;"
                                depressed
                                block
                                @click="getMoreData">
                            <v-progress-circular indeterminate color="primary"></v-progress-circular>
                        </v-btn>
                        <span v-else></span>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import axios from 'axios';

    export default {
        name: "ServiceList",
        data: function () {
            return {
                places: [
                    '모두',
                    '강남구',
                    '강동구',
                    '강북구',
                    '강서구',
                    '관악구',
                    '광진구',
                    '구로구',
                    '노원구',
                    '도봉구',
                    '동대문구',
                    '동작구',
                    '마포구',
                    '서대문구',
                    '서초구',
                    '성동구',
                    '성북구',
                    '송파구',
                    '양천구',
                    '영등포구',
                    '용산구',
                    '은평구',
                    '종로구',
                    '중구',
                    '중랑구'
                ],
                minclass_list: {
                    'sport' : [
                        '모두',
                        '축구장',
                        '다목적경기장',
                        '테니스장',
                        '농구장',
                        '풋살경기장',
                        '배트민턴장',
                        '족구장'
                    ],
                    'institution': [
                        '모두',
                        '시설대관',
                        '강의실',
                        '강당',
                        '다목적',
                        '주민사랑방',
                        '녹화/촬영',
                        '회의실',
                        '기타'
                    ],
                    'culture': [
                        '모두',
                        '전시/관람',
                        '공연/콘서트',
                        '행사/대회'
                    ],
                    'education': [
                        '모두',
                        '자연/과학',
                        '건강/스포츠',
                        '취미',
                        '역사',
                        '체험/견학',
                        '녹화/촬영',
                        '교양',
                        '취미레저',
                        '기타'
                    ],
                    'medical': [
                        '모두'
                    ]
                },
                ServicesData: [],
                MAXCLASSNM: '',
                MINCLASSNM: '',
                AREANM: '',
                page: 1,
                isGetMore: 0,
                recommend: []
            }
        },
        created: function() {
            this.MAXCLASSNM = this.$store.state.category;

            var jArray = new Array();
            var self = this;

            // 추천상품
            axios.post('/recommend')
                .then(function (response) {
                    console.log( response.data );


                    for (var idx in response.data) {
                        self.recommend.push( response.data[idx] );
                    }

                    console.log( self.recommend );
                })
                .catch(function (error) {
                    console.log(error);
                    return [];
                });

            axios.post('/services?page=' + this.page, {MAXCLASSNM: this.$store.state.category, page: this.page})
                .then(function (response) {

                    console.log(response);

                    for (var idx in response.data.data) {
                        jArray.push( response.data.data[idx] );
                    }
                })
                .catch(function (error) {
                    console.log(error);

                    return [];
                });

            this.ServicesData = jArray;

            console.log(this.ServicesData);
        },
        computed: {
            computedCategoryName() {
                let cate = this.$store.state.category;
                let result = '';

                if (cate === '교육') {
                    result = '교육신청';
                } else if (cate === '진료') {
                    result = '진료예약';
                }else {
                    result = cate;
                }

                return result;
            },

            computedMinclasses() {
                if ( this.MAXCLASSNM === '교육' ) {
                    return this.minclass_list['education'];
                } else if ( this.MAXCLASSNM === '체육시설' ) {
                    return this.minclass_list['sport'];
                } else if ( this.MAXCLASSNM === '시설대관' ) {
                    return this.minclass_list['institution'];
                } else if ( this.MAXCLASSNM === '문화행사' ) {
                    return this.minclass_list['culture'];
                } else {
                    return this.minclass_list['medical'];
                }
            }
        },
        methods: {
            getSearchedData : function (event) {
                console.log( this.AREANM );
                console.log( this.MINCLASSNM );

                var jArray = new Array();
                var self = this;
                axios.post('/services?page=' + this.page, {MAXCLASSNM: this.MAXCLASSNM, AREANM: this.AREANM, MINCLASSNM: this.MINCLASSNM, page: this.page})
                    .then(function (response) {
                        console.log(response);

                        for (var idx in response.data.data) {
                            jArray.push( response.data.data[idx] );
                        }

                    })
                    .catch(function (error) {
                        console.log(error);

                        return [];
                    });

                this.ServicesData = jArray;
            },
            getMoreData : function (event) {
                this.page += 1;

                var self = this;

                self.isGetMore = 1;
                axios.post('/services?page=' + this.page, {MAXCLASSNM: this.MAXCLASSNM, AREANM: this.AREANM, MINCLASSNM: this.MINCLASSNM})
                    .then(function (response) {
                        self.isGetMore = 0;
                        console.log(response);
                        for (var idx in response.data.data) {
                            self.ServicesData.push( response.data.data[idx] );
                        }

                        if( response.data.data.length < 25 ) {
                            self.isGetMore = 2;
                        }
                    })
                    .catch(function (error) {
                        console.log(error);

                        return [];
                    });
            },
            goToDetail : function (SVCID) {
                this.$store.state.service = SVCID;

                this.$router.push('service')
            }
        }
    }
</script>

<style scoped>

</style>