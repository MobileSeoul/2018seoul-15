<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;
use App\Service;

class ServiceController extends Controller
{
    public function servicies(Request $request)
    {
        $servicies = DB::table('servicies');

        if( $request->has('MAXCLASSNM') ) {
            $servicies = $servicies->where('MAXCLASSNM', $request->MAXCLASSNM);
        }
        if( $request->has('MINCLASSNM') ) {
            if ( $request->MINCLASSNM != null && $request->MINCLASSNM != "모두" ) {
                $servicies = $servicies->where('MINCLASSNM', $request->MINCLASSNM);
            }
        }
        if( $request->has('AREANM') ) {
            if ( $request->AREANM != null && $request->AREANM != "모두" ) {
                $servicies = $servicies->where('AREANM', $request->AREANM);
            }
        }
        //$services = $services->where('SVCSTATNM', '접수중')->orWhere('SVCSTATNM', '안내중')->get();

        $servicies = $servicies
            ->where('SVCID', 'LIKE', "S%")
            ->where('IMG_URL', '!=' , "''")
            ->where('SVCSTATNM', '접수중')
            //->whereIn('SVCSTATNM', ['접수중', '안내중'])
            ->paginate(25);
        return $servicies->toJson(JSON_UNESCAPED_UNICODE);
    }

    public function service(Request $request)
    {
        $SVCID = $request->SVCID;

        $service = Service::where('SVCID', $SVCID)->first();
        return $service->toJson(JSON_UNESCAPED_UNICODE);
    }

    public function recommendService(Request $request)
    {
        $services = Service::where('SVCSTATNM', '접수중')
            ->where('SVCID', 'LIKE', "S%")
            ->where('IMG_URL', '!=' , "''")
            ->get();
        $random = $services->random(5);

        return $random;
    }
}
