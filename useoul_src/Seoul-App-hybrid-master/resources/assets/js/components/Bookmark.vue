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
                    <h1 style="color: white;">즐겨찾기</h1>
                </v-flex>
            </v-layout>
        </v-toolbar>
        <v-content>
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
        </v-content>
    </v-app>
</template>

<script>
    export default {
        name: "Bookmark",
        data: function() {
            return {
                ServicesData: [],
            }
        },
        mounted: function() {
            window.seoulApp.updateList();
        },
        methods: {
            updateBookmarkedList : function( list ) {
                self = this;

                for(var l in list)
                {
                    axios.post('/service', { SVCID: list[l] })
                        .then(function (response) {
                            self.ServicesData.push(response.data);
                        })
                        .catch(function (error) {
                            console.log(error);
                        });
                }
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