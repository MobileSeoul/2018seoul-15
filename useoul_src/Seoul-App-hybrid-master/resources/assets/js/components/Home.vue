<template>
    <v-app class="main">
        <v-content>
            <v-container fluid style="padding:0; background-color: #eee;">
                <v-layout row wrap style="padding-bottom: 20px;">
                    <v-flex xs12>
                        <v-img
                                :src="`/storage/main.jpg`"
                        ></v-img>
                    </v-flex>
                    <v-flex xs12 style="margin-top: -30px; padding-left: 10px;">
                        <v-layout row wrap>
                            <v-flex xs6 class="box-card">
                                <v-card color="white" class="card-serivce" hover @click.native="goToServices('체육시설')">
                                    <v-avatar size="96">
                                        <img
                                                src="/storage/sports.jpg"
                                                alt=""
                                        >
                                    </v-avatar>
                                    <v-card-text class="card-service-text">
                                        <h3>체육시설</h3>
                                        <p>체육시설 예약하기</p>
                                    </v-card-text>
                                </v-card>
                            </v-flex>
                            <v-flex xs6 class="box-card">
                                <v-card color="white" class="card-serivce" hover @click.native="goToServices('시설대관')">
                                    <v-avatar size="96">
                                        <img
                                                src="/storage/institution.jpg"
                                                alt=""
                                        >
                                    </v-avatar>
                                    <v-card-text class="card-service-text">
                                        <h3>시설대관</h3>
                                        <p>공공시설 대관하기</p>
                                    </v-card-text>
                                </v-card>
                            </v-flex>
                            <v-flex xs6 class="box-card">
                                <v-card color="white" class="card-serivce" hover @click.native="goToServices('교육')">
                                    <v-avatar size="96">
                                        <img
                                                src="/storage/education.jpg"
                                                alt=""
                                        >
                                    </v-avatar>
                                    <v-card-text class="card-service-text">
                                        <h3>교육신청</h3>
                                        <p>교육 신청하기</p>
                                    </v-card-text>
                                </v-card>
                            </v-flex><v-flex xs6 class="box-card">
                            <v-card color="white" class="card-serivce" hover @click.native="goToServices('문화행사')">
                                <v-avatar size="96">
                                    <img
                                            src="/storage/culture.jpg"
                                            alt=""
                                    >
                                </v-avatar>
                                <v-card-text class="card-service-text">
                                    <h3>문화행사</h3>
                                    <p>문화행사 참여하기</p>
                                </v-card-text>
                            </v-card>
                        </v-flex>
                            <v-flex xs6 class="box-card">
                                <v-card color="white" class="card-serivce" hover @click.native="goToServices('진료')">
                                    <v-avatar size="96">
                                        <img
                                                src="/storage/medical.jpg"
                                                alt=""
                                        >
                                    </v-avatar>
                                    <v-card-text class="card-service-text">
                                        <h3>진료예약</h3>
                                        <p>진료 예약하기</p>
                                    </v-card-text>
                                </v-card>
                            </v-flex>
                            <v-flex xs6 class="box-card">
                                <v-card color="white" class="card-serivce" hover @click.native="goToServices('즐겨찾기')">
                                    <v-layout
                                            align-center
                                            justify-space-around
                                            wrap
                                    >
                                        <v-avatar size="96" color="teal">
                                            <v-icon large color="white">favorite</v-icon>
                                        </v-avatar>
                                    </v-layout>
                                    <v-card-text class="card-service-text">
                                        <h3>즐겨찾기</h3>
                                        <p>내가 한 즐겨찾기</p>
                                    </v-card-text>
                                </v-card>
                            </v-flex>
                        </v-layout>
                    </v-flex>
                </v-layout>

                <v-layout>
                    <v-flex
                        xs12
                        style="text-align: center; height:30px; line-height:30px; vertical-align: middle; background-color: #aaa;"
                    ><span style="color:white;">Copyright @ co-ravel</span>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    export default {
        name: "Home",
        computed: {

        },
        methods: {
            goToServices : function(category) {
                this.$store.state.category = category;
                if( category === '즐겨찾기' ) {
                    this.$router.push('bookmark');
                } else {
                    this.$router.push('services');
                }
            }
        }
    }
</script>

<style scoped>
    * {
        text-decoration: none;
    }

    .box-card {
        padding-right: 10px;
        padding-top: 10px;
    }

    .card-serivce {
        text-align: center;
        padding-top: 20px;
    }

    .card-serivce .card-service-text {
        padding-bottom: 5px;
    }

    .card-serivce:hover {
        background-color: #eee !important;
    }
</style>