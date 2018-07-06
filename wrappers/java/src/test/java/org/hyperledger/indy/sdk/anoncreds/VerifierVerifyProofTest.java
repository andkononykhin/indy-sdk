package org.hyperledger.indy.sdk.anoncreds;

import org.hyperledger.indy.sdk.InvalidStructureException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VerifierVerifyProofTest extends AnoncredsIntegrationTest {

	private String credentialDef = "{" +
			"\"ver\":\"1.0\"," +
			"\"id\":\"NcYxiDXkpYi6ov5FcYDi1e:3:CL:NcYxiDXkpYi6ov5FcYDi1e:2:gvt:1.0\"," +
			"\"schemaId\":\"NcYxiDXkpYi6ov5FcYDi1e:2:gvt:1.0\"," +
			"\"type\":\"CL\"," +
			"\"tag\":\"tag1\"," +
			"\"value\":{" +
			"   \"primary\":{" +
			"       \"n\":\"90502901707537035761603359102972927015094344887671608473563048546399318843145854729384094097125880227799866026686300876969620244718571706730630487142817674697452073102192752051196060218926539989778592290630647177519178436533550223192561612158583048899284958551027127491141688406309178312794479780395092575529229581812915117683391971563795764029072562826012120146268791587368268338520038265414770129610741281171523320475100949367313774276668340383295318400111641358742963591754301429669639739223276118497403061859733977429014695728224361571918732882994520863331988259643585231391945606660054756033475349888553941871633\"," +
			"       \"s\":\"16079962022659642737880964308643040221633375727852777540249619868840748934687121177414651291735970733576960001100180719510833447376696752277655175973441390852386058061170594932412173680294856832311652446995095913574755665567745465686396848383240740664114091353792807986565022495145896045788870387268736558441959279315923694455427502320687382690021420146475094177438209232661180741720855857843514388139350603712554963424551589126819364616676401858453942584469027422300566517144646923043579406863141239816535111900942376620245748820776057212827542177399935672608318267248510734088467154880819001925249876010542136306400\"," +
			"       \"r\":{" +
			"           \"height\":\"31650208165261227377582116230245251505068622293801640684361082928520824724406884584685159593590813236268329017239969546482280457802950573283561913634594848517386238804573309808905504307481588276481820827403639037429749442116509624667553788725638502484107121144435446866347658791104179209435492588670467158628431265575648717871122377080353563202414848273874552691958458236385620435223102274748276788837012392197235563544685203067504076860625713864127996091204010848082028953001163129123709636237311103291716277712943137073550588599516789839253241030742599543909335575346250521011625231258731414192246742443383607392353\"," +
			"           \"master_secret\":\"74679274886814353719555937676742740317459761538012406648265688344083642186728001620347452439699203115723381351613475618431966057932178657857117361447274584171186778770304908019594060793992002196212535357638829127663486981707489828802327476646573525286190387752128700845093362195167066424997286542820756737340566261729954191777430103373839280177568949985025382215332556527855775245772072178452908166233286496100723181580844264722375994554306471982550429097017409567333847912047614528965232214222404971516665673879637383929894860792676415489215171323954742672787316658285896640897826795013878488970435518403749049188353\"," +
			"           \"name\":\"33198767640741246930732453225834809433716992853259764548974545059463451498495370944201487887577242158790872065273587058509043320166570197911039666897121870464040484153770146990367615634597523645329269987339281350967912586139844312971300310087112199839294332951144954113948567853589201207544861118788964341821364710468640981063367399342595422747985253624731912725252503684550929412607786901313147325778843368012563048552834546671508039915246050597697657885568903742662447255139428616451850587769858680830492305556158510957477548133237644367698056000691277015718859479619417702688772234819439341248413782985264316110905\"," +
			"           \"age\":\"61525076208718615759927510394677547801851312063294988146503553020744199866454917195962908989889745217693077918345124053832017576211047794675880350429370120232936677552853759068244989356069255467942362804273782285103067198688566667012545402665399307841433911777648347757866650198470817782960437084413924187108698242096752447418865739647157418558583210979951983242270462647920858888633037859837988695336031617362457607939461602723616600293190667438448629853566541591474937732673768602190133588621597183776382214208417168313856191737259569366765904630316093083150241316585481424313293043841306375238240194067140161501557\"," +
			"           \"sex\":\"54772315956779183828438137197803114690721477816629422239263906750466695709783032065525967692586100899340078769903359930530709315603870023175191564991469974340416845558602185159362438249926836436454710457963138142120320476411269053046421897005573038041108114634096448485001671707627035969962653941768011592131549658598657111294220194340855201357465534768629408210193234716297662604975296864594967277003731322553137209262390016566287407240989710848376524240514807688179007828842875344712404597199185758782456373957737246283533386066033482510820680564590716188940090725528668760706100620073888573678184320449789671579510\"" +
			"       }," +
			"       \"rctxt\":\"76892043260555894770560400084557663478280447194126340117817641017140640839192728431445419660424386224267486001064942189809740481384838882411264356253632632932965850200101888447172439394420401738038416096015409633619548420198150158905416580861958047764687147320966286034552119622249985091598103130044556399579944606980932700636974450045081162085975548457797751885303046348381756432637289539317901776994168150715295389809286199016245338504145512251948980299662593016106896308538559864722157477100590286404096504818259680338494320107571926742939358273269863350713300942114380241358256572728884894084242845802773245211768\"," +
			"       \"z\":\"50743545657776898907335487912248014894865817755898232883786985746684155475271365940248406597150335135169332914195448975261214427097056816340864933148634575536150568257691651094002821775602888479753654854953792640670179868381456147016548946433865690642039359615076183826285025149789692446822192607695101269741556565178163780430767239399304233408281868228220155212856433055209168521403567512151187905178836660278360220310645931105887197070627670695751278747608818466727463722803244270793543114276039056615826012529839350299457624142251892920028939700750556810870977181572515459663715332219809305085763629805615634287917\"" +
			"   }" +
			"}}\n";
	private String proofJson = "{" +
			"\"proof\":{" +
			"   \"proofs\":[" +
			"       {" +
			"           \"primary_proof\":{" +
			"               \"eq_proof\":{" +
			"                   \"revealed_attrs\":{\"name\":\"1139481716457488690172217916278103335\"}," +
			"                   \"a_prime\":\"43327609500152908428687962171696405871707571667204761124012842119343153099783235160599162496493301885875638169683057270045881750225483651557366417167650957594365349026909948144814639502433987704349454784281894657004346106199563684088673987193121289853246856661528282530099192375683019699636368604465115373192003639576141258475744093922741603046560946878870177953983101843587195740439104717972863308559418798846125318316332522236151860187400386341390510882589866504268064174055710854378652450019043254630091715307262736209176850043076770448053729356651112131132578260073395587639570277942508624930736320251752663210038\"," +
			"                   \"e\":\"79037721147976537410527732133494629735677017273969343145245263012714594693437362688523597880300140255260627266512904124527635100641418317\"," +
			"                   \"v\":\"214225813157112220338299191272659603692918476031192864724847351567150114489777357244743651234241043839153479057692446807908027931427597739215418806902834161845020185008615410052615653180413393749282819481250403708378344731005565322211265935695209198027753556039025336903504697160864083626575197039677705701641040296292558766806691483181225948020321391505693630467735638015153103952143908012250058645199469678300451674336390914532483774619703005805190003628501661923662438899648255201991061960266732222957328471450275898389749105465945937191212333717344581877678929228700511369857340716579227571338592527229506438872104750967328270843803308898206523975751175942816697074056831209189143623615256223703100311777501519190037096358239111454134975888395882678989715066789827440034156747702420360267861722395291167923269675242299767965128365711248493962877200080961604268254063822608912891870474856619142825207031067815845825840\"," +
			"                   \"m\":{\"master_secret\":\"8166706406337265746410080683947840789411493595228812517273034968361634232630990342290143920223422316951023754697867665512030988006075007020458652847790129251563145498413730080038\",\"age\":\"6373638952233761580551986622562857053353409992743861082015837561463411483032103805526916969445625140770087167956924144944168419597634075434714755580877413025763490684471063075702\",\"sex\":\"4157618527711441550812711354620679606088848728791239625735174950980410117013192701472665366162706066037025180214168116271733116296660860521399539876318249516375349103711237907683\",\"height\":\"5784630554176837045550266770433185481185841832148891699617962470689880934864768122034725317085799414380037250478160489805053263774167522729570992256981057796091121322441547650043\"}," +
			"                   \"m2\":\"7829424341636852104685077778976128918618602580444249022507920565573285240201734319190283324898190158016888293926901799073594151932615944951493921028520308567066541810861735308129\"" +
			"               }," +
			"               \"ge_proofs\":[]" +
			"           }," +
			"           \"non_revoc_proof\":null" +
			"       }" +
			"   ]," +
			"   \"aggregated_proof\":{" +
			"       \"c_hash\":\"94928500966171184580282772603025462450965970926914190910851357668731871173513\"," +
			"       \"c_list\":[[1,87,56,133,10,65,186,34,119,18,230,17,147,209,124,179,28,175,7,205,214,218,65,244,31,138,193,126,136,136,123,91,1,185,64,164,64,252,222,87,233,164,69,103,212,41,186,138,107,134,42,0,100,192,153,11,24,254,7,6,0,240,140,57,118,40,118,147,165,152,123,37,115,252,90,68,147,99,65,125,139,25,53,23,210,175,229,213,64,128,158,109,70,229,28,89,178,115,184,67,203,137,40,236,42,91,47,139,179,6,80,59,255,170,150,70,163,42,200,70,187,176,50,38,49,14,221,253,244,193,177,154,136,76,41,232,54,114,162,230,110,165,186,235,218,150,247,243,51,87,6,224,167,175,124,6,176,182,237,152,20,40,248,113,65,148,243,53,78,35,55,137,103,82,188,112,184,87,236,140,136,198,153,202,146,121,123,1,127,76,208,181,155,111,163,13,168,143,24,124,0,104,140,139,235,180,247,75,230,145,230,82,125,198,143,132,40,173,129,199,34,118,23,226,163,173,136,69,162,137,31,242,47,71,40,2,207,206,234,108,43,181,232,19,172,0,176,132,7,51,124,1,152,4,239,216,54]]" +
			"       }" +
			"   }," +
			"   \"requested_proof\":{" +
			"       \"revealed_attrs\":{\"attr1_referent\":{\"sub_proof_index\":0,\"raw\":\"Alex\",\"encoded\":\"1139481716457488690172217916278103335\"}},\"self_attested_attrs\":{},\"unrevealed_attrs\":{},\"predicates\":{}},\"identifiers\":[{\"schema_id\": \"\",\"cred_def_id\": \"\",\"rev_reg_id\":null,\"timestamp\":null}" +
			"   ]" +
			"}\n";

	private String verifierProofRequest = new JSONObject("{\n" +
			"                   \"nonce\":\"123432421212\",\n" +
			"                   \"name\":\"proof_req_1\",\n" +
			"                   \"version\":\"0.1\", " +
			"                   \"requested_attributes\":{" +
			"                          \"attr1_referent\":{\"name\":\"name\"}" +
			"                    },\n" +
			"                    \"requested_predicates\":{}" +
			"               }").toString();

	@Test
	public void testVerifierVerifyProofWorksForCorrectProof() throws Exception {
		String schemasJson = new JSONObject().put(gvtSchemaId, new JSONObject(gvtSchema)).toString();
		String credentialDefsJson = new JSONObject().put(issuer1gvtCredDefId, new JSONObject(credentialDef)).toString();
		String revocRegDefsJson = new JSONObject().toString();
		String revocRegsJson = new JSONObject().toString();

		proofJson = proofJson.replace("\"schema_id\": \"\"", String.format("\"schema_id\": \"%s\"", gvtSchemaId));
		proofJson = proofJson.replace("\"cred_def_id\": \"\"", String.format("\"cred_def_id\": \"%s\"", issuer1gvtCredDefId));

		Boolean valid = Anoncreds.verifierVerifyProof(verifierProofRequest, new JSONObject(proofJson).toString(), schemasJson, credentialDefsJson, revocRegDefsJson, revocRegsJson).get();
		assertTrue(valid);
	}

	@Test
	public void testVerifierVerifyProofWorksForProofDoesNotCorrespondToProofRequest() throws Exception {
		thrown.expect(ExecutionException.class);
		thrown.expectCause(isA(InvalidStructureException.class));

		String schemasJson = new JSONObject().put(gvtSchemaId, new JSONObject(gvtSchema)).toString();
		String credentialDefsJson = new JSONObject().put(issuer1gvtCredDefId, new JSONObject(credentialDef)).toString();
		String revocRegDefsJson = new JSONObject().toString();
		String revocRegsJson = new JSONObject().toString();

		proofJson = proofJson.replace("\"schema_id\": \"\"", String.format("\"schema_id\": \"%s\"", gvtSchemaId));
		proofJson = proofJson.replace("\"cred_def_id\": \"\"", String.format("\"cred_def_id\": \"%s\"", issuer1gvtCredDefId));

		String proofRequest = new JSONObject("{\"nonce\":\"123432421212\",\n" +
				"               \"name\":\"proof_req_1\",\n" +
				"               \"version\":\"0.1\",\n" +
				"               \"requested_attributes\":{\"attr1_referent\":{\"name\":\"sex\"}},\n" +
				"               \"requested_predicates\":{\"predicate1_referent\":{\"name\":\"height\",\"p_type\":\">=\",\"p_value\":180}}\n" +
				"              }").toString();

		Anoncreds.verifierVerifyProof(proofRequest, new JSONObject(proofJson).toString(), schemasJson, credentialDefsJson, revocRegDefsJson, revocRegsJson).get();
	}

	@Test
	public void testVerifierVerifyProofWorksForWrongProof() throws Exception {

		String schemasJson = new JSONObject().put(gvtSchemaId, new JSONObject(gvtSchema)).toString();
		String credentialDefsJson = new JSONObject().put(issuer1gvtCredDefId, new JSONObject(credentialDef)).toString();
		String revocRegDefsJson = new JSONObject().toString();
		String revocRegsJson = new JSONObject().toString();

		proofJson = proofJson.replace("\"schema_id\": \"\"", String.format("\"schema_id\": \"%s\"", gvtSchemaId));
		proofJson = proofJson.replace("\"cred_def_id\": \"\"", String.format("\"cred_def_id\": \"%s\"", issuer1gvtCredDefId));
		proofJson = proofJson.replace("9492850096", "1111111111");

		Boolean valid = Anoncreds.verifierVerifyProof(verifierProofRequest, new JSONObject(proofJson).toString(), schemasJson, credentialDefsJson, revocRegDefsJson, revocRegsJson).get();
		assertFalse(valid);
	}

	@Test
	public void testVerifierVerifyProofWorksForInvalidProofJson() throws Exception {
		thrown.expect(ExecutionException.class);
		thrown.expectCause(isA(InvalidStructureException.class));

		String schemasJson = new JSONObject().put(gvtSchemaId, new JSONObject(gvtSchema)).toString();
		String credentialDefsJson = new JSONObject().put(issuer1gvtCredDefId, new JSONObject(credentialDef)).toString();
		String revocRegDefsJson = new JSONObject().toString();
		String revocRegsJson = new JSONObject().toString();

		String proofJson = String.format("{\"proofs\":{\"issuer1GvtCredential::277478db-bf57-42c3-8530-b1b13cfe0bfd\":{\"primary_proof\":{\"eq_proof\":{\"revealed_attrs\":{\"name\":\"1139481716457488690172217916278103335\"},\"a_prime\":\"80401564260558483983794628158664845806393125691167675024527906210615204776868092566789307767601325086260531777605457298059939671624755239928848057947875953445797869574854365751051663611984607735255307096920094357120779812375573500489773454634756645206823074153240319316758529163584251907107473703779754778699279153037094140428648169418133281187947677937472972061954089873405836249023133445286756991574802740614183730141450546881449500189789970102133738133443822618072337620343825908790734460412932921199267304555521397418007577171242880211812703320270140386219809818196744216958369397014610013338422295772654405475023\",\"e\":\"31151798717381512709903464053695613005379725796031086912986270617392167764097422442809244590980303622977555221812111085160553241592792901\",\"v\":\"524407431684833626723631303096063196973911986967748096669183384949467719053669910411426601230736351335262754473490498825342793551112426427823428399937548938048089615644972537564428344526295733169691240937176356626523864731701111189536269488496019586818879697981955044502664124964896796783428945944075084807859935155837238670987272778459356531608865162828109489758902085206073584532002909678902616210042778963974064479140826712481297584040209095459963718975102750913306565864485279810056629704077428898739021040190774575868853629858297299392839284660771662690107106553362040805152261505268111067408422298806905178826507224233050991301274817252924123120887017757639206512015559321675322509820081151404696713509158685022511201565062671933414307463988209696457343022378430051265752251403461414881325357657438328740471164157220698425309006894962942640219890219594168419276308074677144722217081026358892787770650248878952483621\",\"m\":{\"age\":\"10477979077744818183854012231360633424177093192344587159214818537659704987539982653663361680650769087122324965941845552897155693994859927792964720675888893623940580527766661802170\",\"sex\":\"15368219775809326116045200104269422566086585069798988383076685221700842794654771075432385446820819836777771517356551059931242867733879324915651894894695726945279462946826404864068\",\"height\":\"268172143999991481637372321419290603042446269013750825098514042757459298040087626745653681785038933035820421862976371452111736537699176931068992453946771945552540798204580069806\"},\"m1\":\"119095745403940293668103184388411799541118279558928018597628509118163496000813590825371995586347826189221837428823000332905316924389185590810015031744029496470545254805993327676570037596326743185389101389800942263689809725968264069601565478411709555274081560719927118853299543998608664701485475703881376151770\",\"m2\":\"3166313665375815600922385342096456465402430622944571045536207479553790085339726549928012930073803465171492637049498407367742103524723152099973753540483894420905314750248333232361\"},\"ge_proofs\":[{\"u\":{\"2\":\"6494171529848192644197417834173236605253723188808961394289041396341136802965710957759175642924978223517091081898946519122412445399638640485278379079647638538597635045303985779767\",\"0\":\"7739508859260491061487569748588091139318989278758566530899756574128579312557203413565436003310787878172471425996601979342157451689172171025305431595131816910273398879776841751855\",\"3\":\"9424758820140378077609053635383940574362083113571024891496206162696034958494400871955445981458978146571146602763357500412840538526390475379772903513687358736287298159312524034159\",\"1\":\"9011979414559555265454106061917684716953356440811838475257096756618761731111646531136628099710567973381801256908067529269805992222342928842825929421929485785888403149296320711642\"},\"r\":{\"DELTA\":\"2119857977629302693157808821351328058251440215802746362450951329352726877165815663955490999790457576333458830301801261754696823614762123890412904169206391143688952648566814660498520188221060505840151491403269696751525874990487604723445355651918681212361562384420233903265612599812725766212744963540390806334870022328290970137051148373040320927100063898502086531019924715927190306801273252711777648467224661735618842887006436195147540705753550974655689586750013569294343535843195025962867299786380033532422131203367401906988124836294104501525520053613392691214421562815044433237816093079784307397782961917892254668290115653012265908717124278607660504580036193346698672079435538219972121355893074219968755049500875222141\",\"2\":\"879097501989202140886939888802566536179834329508897124489020677433754766947767937608431979796722207676629625451150104784909666168153917345813160237337412296010679353735699663083287427507870244565918756969618964144516025526404618052053542009438548457492400344119561349471929199757453154204191407620539220514897529346602664135146454509169680801061111878075145734123580343470361019624175036825631373890661124315134340427076598351080893567995392248394683875116715114577054906406649006122102488431184007790011073389768061904597267545895265921673106871142463561948479668876241841045522543174660428236658891636170119227855493059358614089146415798861053408542832475696099851160385105386001523305465829676723036394820593263477\",\"0\":\"1724016272047416140958096373304304971004826284109046259544344355102178044512441391364907122486655755929044720001281832600729467778103556397960700809066582436321515744527550472324028227472294258045699756170293405547851344921626775854114063087070898499913846456795761213291925373770081490280103876827479351849800210782799381740073719081199000612284788683993320623339686128531187019125095700122135094060470612862911102824801065698176788174959069186600426519872015152034176356923049531650418553748519941342115963599848111324793380438600664408464987023646615003553912544410140730587797458882329021327455905737414352355326238028222782957735440607899424838572541602600159016542488644761584240884783618700311735467659132540546\",\"3\":\"2317535203964314926167241523636020444600002667629517624482931328850422196008281300859516069440995466415138723103558631951648519232327284208990029010060986032518946759289078833125920310350676484457972303378558158127406345804560689086460633931717939234025886786468170219981598030245042011840614339386724945679531091642132820284896626191109974537171662283750959028046143650291367908660204201563611944187723824430780626387525165408619587771059635528553832034409311888615502905143628507219523591091412192645348525327725381323865648645828460581593542176351568614465903523790649219812666979685223535464526901006270478687017672202058914176692964406859722580270696925877498058525086810338471380117323227744481903228027847825795\",\"1\":\"1119193929864813751243160041764170298897380522230946444206167281178657213260394833843687899872857393015947283159245092452814155776571829885921814072299525859857844030379558685168895306445277750249341844789101670896570226707650318347992386244538723699686941887792682779028216548922683313576597384354842537728667739985216662699631842296096507821667149950956179957306177525178260912379909156360834120816956949271530622510333943914411903103069247646327625753995178999023427645468623522280255892736633780185163496867644317005801241786702434621502492159672660131289312665511793827552317714835658019088880972220344126692027952749318018900669839090109361161616086319604439015851316798257015063653414161203599184730094765941653\"},\"mj\":\"10477979077744818183854012231360633424177093192344587159214818537659704987539982653663361680650769087122324965941845552897155693994859927792964720675888893623940580527766661802170\",\"alpha\":\"46280660038407959140964701167450659223532556136388451390393713283900546119670373626221864441898929302821705811144923685080534692512705456699843367809872982836890616398604933641265111106644805368974824737276965928297120628041257593166650593538539384316563258781595629888673792430276007730792093088812056156937735120078929629310611907731935101448992312370312134173482115524436767558802102266208152808607480693236511858269018733175523724309089010048330044458187371675333889670055578652283806685440133357512406700879353713629795062705271430695988191782837658895477702634883214188598350625843489120361660836956958750828038278027538830855628653513539929730230905015331221220017847248793929813230252015802389329428995718799619565984669228143200627972926117282688854152516298117476837960100343260648687249027349308513966440386556698667484082658689\",\"t\":{\"DELTA\":\"46814992964714978733007076702016837564951956529003697497847838781899848384824991374342901164708655443686022921583406187082133141084994843502230809550055933825660668160300304112671478218513259983054489597176651737200716259733573469298437873515151377206364940530308167934399245072298875358347931404742292788785586833114480704138718996633638362933821933388459210678374952072108333767698704767907612549860590824123780096225591372365712106060039646448181221691765233478768574198237963457485496438076793333937013217675591500849193742006533651525421426481898699626618796271544860105422331629265388419155909716261466161258430\",\"2\":\"59423006413504086085782234600502410213751379553855471973440165009200961757474676407242673622935614782362911290590560535490636029324125251850583605745046201217673654522625983661578962623803698461459190578519097656221453474955879823750445359506290522280566225253310030053812918275525607874059407284653434046369835156477189219911810464401689041140506062300317020407969423270374033482533711564673658146930272487464489365713112043565257807490520178903336328210031106311280471651300486164966423437275272281777742004535722142265580037959473078313965482591454009972765788975683031385823798895914265841131145707278751512534120\",\"0\":\"56510878078818710798555570103060159621941668074271797077206591818472978018558098567975838757566260370093327989369045722406190165972775356924844244889146946158949660988214890388299203816110339909687790860564719380865809705044646711632599987968183128514431910561478715003212633874423067294596323864121737000450543142072142652163818450299889830999149821558252183477517484127000480272695698860647674027831262149565273068850774090998356019534296579838685977022988536930596918054160990243868372150609770079720240227817149126735182138479851227052696211125454858584118346950878092387488482897777914362341820607560926173967363\",\"3\":\"63511079416489489495396586813126304469185174450150717746314545118902972011091412254834718868134635251731510764117528579641756327883640004345178347120290107941107152421856942264968771810665927914509411385404403747487862696526824127219640807008235054362138760656969613951620938020257273816713908815343872804442748694361381399025862438391456307852482826748664499083370705834755863016895566228300904018909174673301643617543662527772400085378252706897979609427451977654028887889811453690146157824251379525221390697200211891556653698308665831075787991412401737090471273439878635073797691350863566834141222438011402987450926\",\"1\":\"30348838247529448929141877305241172943867610065951047292188826263950046630912426030349276970628525991007036685038199133783991618544554063310358191845473212966131475853690378885426974792306638181168558731807811629973716711132134244797541560013139884391800841941607502149630914097258613821336239993125960064136287579351403225717114920758719152701696123905042695943045383536065833292374624566478931465135875411483860059753175449604448434619593495399051968638830805689355610877075130302742512428461286121237297212174164897833936610857614962734658136750299346971377383141235020438750748045568800723867413392427848651081274\"},\"predicate\":{\"attr_name\":\"age\",\"p_type\":\"GE\",\"value\":18}}]},\"non_revoc_proof\":null}},\"aggregated_proof\":{\"c_hash\":\"81135772044295974649282368084258333955993271555081206390568996949836231116301\",\"c_list\":[[2,124,231,47,189,36,247,160,61,220,165,35,97,165,203,185,133,253,81,239,67,127,156,49,189,16,140,30,177,161,221,54,154,0,127,143,98,212,114,193,188,85,206,171,198,140,9,192,10,254,218,120,201,182,40,141,80,35,81,148,204,192,41,5,186,33,50,77,211,163,124,130,32,219,193,167,79,43,181,76,19,249,53,79,70,221,205,36,180,50,120,255,161,227,196,204,71,106,221,131,220,7,73,86,128,208,48,58,123,63,82,24,170,141,143,56,221,96,151,108,105,38,185,243,224,112,177,101,195,87,208,201,39,123,165,125,92,104,234,188,54,92,31,158,178,152,52,205,26,156,237,241,23,15,76,220,168,32,175,230,157,197,225,70,57,237,8,81,13,17,95,70,143,56,162,223,203,8,48,153,51,51,118,116,32,139,187,222,146,86,165,111,125,107,203,18,212,28,168,22,62,69,204,207,122,148,25,30,92,120,83,214,116,221,204,120,230,70,128,139,181,110,69,93,253,240,69,16,113,224,246,41,142,0,83,237,186,4,50,156,206,199,89,74,96,168,249,240,101,16,103,234,162,219,52,218,207],[1,191,167,2,151,36,61,136,184,172,120,86,127,88,109,119,56,21,167,171,217,221,24,64,246,237,255,152,81,183,201,191,59,234,213,101,254,91,33,205,120,71,215,144,160,243,145,109,19,151,241,46,135,132,50,143,219,207,197,35,89,103,83,212,96,83,222,101,55,57,220,161,252,115,39,62,46,160,30,138,221,89,125,66,114,150,5,95,63,10,55,107,102,73,40,69,41,6,57,0,64,226,152,66,181,149,251,50,28,53,18,26,221,5,188,67,125,184,190,200,56,92,132,201,242,211,37,2,43,6,146,88,228,120,204,190,4,118,134,106,118,110,249,145,175,165,116,197,200,183,207,215,197,79,207,203,29,182,231,151,248,233,107,41,79,234,250,27,33,33,107,102,240,47,37,230,243,185,93,192,52,31,73,211,11,173,150,92,194,154,172,247,221,206,129,85,193,105,172,140,201,40,240,200,28,94,1,96,204,175,113,170,46,134,229,111,215,208,237,252,84,50,249,41,214,79,38,194,23,212,7,164,153,217,23,252,32,114,145,58,189,118,104,131,84,184,115,175,199,227,219,117,23,113,113,180,3],[240,104,187,71,84,144,129,123,12,181,215,233,27,55,56,54,94,57,17,42,111,42,112,234,192,23,226,103,118,198,189,175,175,1,102,64,128,100,221,201,134,106,83,239,69,43,150,172,95,206,145,224,207,239,39,193,30,200,90,125,175,125,59,47,250,224,193,21,64,112,101,131,128,249,96,165,73,33,174,64,69,252,209,158,130,53,23,158,217,173,69,51,12,145,70,174,15,206,13,181,50,246,50,110,223,65,250,44,39,33,8,47,169,242,147,3,190,164,110,20,68,5,142,133,38,198,151,161,167,0,219,128,126,120,190,23,153,22,250,78,114,241,252,181,74,142,65,123,225,153,75,159,78,84,28,110,203,105,231,238,75,138,121,233,75,163,221,69,106,143,1,217,251,43,147,252,189,122,19,124,189,180,206,91,165,199,41,172,233,102,14,91,162,254,16,142,60,230,39,200,208,236,101,69,101,152,233,217,100,206,31,120,211,191,90,56,205,40,180,120,47,210,224,86,153,34,86,237,204,11,183,227,0,224,15,201,32,228,4,210,43,156,68,246,137,150,103,197,191,150,155,181,78,5,134,58],[1,214,184,139,205,251,132,131,8,186,140,58,211,242,134,120,121,253,128,192,10,252,172,101,44,26,119,56,212,8,248,71,19,96,59,12,233,191,63,187,217,35,191,160,127,247,189,247,229,111,252,101,126,10,142,252,238,215,211,137,137,164,114,186,255,199,183,50,103,9,158,63,134,140,162,154,188,109,52,31,92,78,38,228,0,60,225,100,239,88,114,95,48,71,7,117,168,45,45,177,178,62,87,197,98,174,123,249,26,237,179,12,63,182,46,218,183,148,163,222,179,159,146,56,142,190,122,100,211,6,86,237,10,7,111,186,27,66,95,252,108,247,203,1,111,60,13,218,104,63,128,125,197,11,201,138,33,122,37,31,163,123,120,132,65,122,208,60,80,87,113,183,28,31,74,106,18,79,52,245,113,184,94,202,72,223,8,128,209,43,77,237,119,208,255,144,26,76,223,77,177,131,237,49,150,251,53,150,115,33,254,237,185,15,140,234,205,99,248,252,171,245,192,104,151,194,190,186,249,180,246,9,169,165,0,221,7,107,39,67,58,178,176,99,212,40,247,49,127,7,94,5,170,65,154,28,104],[1,247,26,202,244,120,131,95,151,52,56,38,141,232,178,50,61,45,235,61,12,68,11,180,174,222,110,211,141,253,198,204,248,192,40,99,237,1,45,170,79,208,3,13,135,89,195,65,3,228,224,146,181,198,14,79,78,237,168,81,108,151,68,12,88,242,120,200,120,193,253,51,167,140,43,175,59,18,160,190,233,21,213,135,162,76,38,48,163,110,155,197,97,93,211,183,95,42,172,249,98,59,161,136,70,39,142,48,242,44,154,103,186,161,214,215,0,254,166,150,111,71,242,102,209,125,25,65,144,223,211,137,223,239,50,96,185,171,120,155,171,98,204,23,102,253,68,141,91,240,127,170,199,249,217,165,164,37,174,212,159,232,140,196,216,140,205,102,84,104,220,223,9,249,75,245,78,157,245,203,235,154,73,34,77,12,227,138,93,105,178,114,255,210,88,216,202,64,69,128,220,211,113,51,15,185,103,236,52,187,49,29,162,20,35,21,65,188,33,46,11,172,59,15,221,36,33,213,14,121,36,218,76,80,97,197,83,64,145,73,194,43,233,144,251,86,112,209,230,67,234,116,172,219,123,50,46],[1,114,216,159,37,214,198,117,230,153,15,176,95,20,29,134,179,207,209,35,101,193,47,54,130,141,78,213,54,167,31,73,105,177,129,135,6,135,45,107,103,16,133,187,74,217,42,40,1,214,60,70,78,245,86,82,150,75,91,235,181,249,129,147,202,15,86,250,222,240,203,236,102,39,53,147,79,178,124,184,97,73,65,136,74,29,219,182,83,167,221,203,32,200,243,130,65,234,133,181,203,35,86,21,123,170,74,174,5,132,1,149,77,141,158,193,249,130,37,53,253,234,228,144,66,152,232,246,26,193,6,53,139,45,231,173,115,87,89,61,197,9,96,73,229,189,49,44,203,214,156,139,58,153,77,13,90,35,157,130,184,150,161,69,145,157,4,206,52,216,227,233,113,202,54,154,153,100,83,97,135,88,197,227,42,52,28,221,91,117,56,183,198,102,231,37,232,226,136,142,115,218,175,45,221,143,130,215,184,39,102,172,126,253,152,108,254,241,17,98,70,223,191,138,251,227,243,32,180,190,223,69,135,0,97,105,115,189,221,134,26,159,32,210,172,233,7,65,238,77,203,159,181,188,203,159,190]]}},\"requested_proof\":{\"revealed_attrs\":{\"attr1_referent\":[\"issuer1GvtCredential::277478db-bf57-42c3-8530-b1b13cfe0bfd\",\"Alex\",\"1139481716457488690172217916278103335\"]},\"unrevealed_attrs\":{},\"self_attested_attrs\":{},\"predicates\":{\"predicate1_referent\":\"issuer1GvtCredential::277478db-bf57-42c3-8530-b1b13cfe0bfd\"},\"identifiers\":{\"issuer1GvtCredential::277478db-bf57-42c3-8530-b1b13cfe0bfd\":{\"issuer_did\":\"NcYxiDXkpYi6ov5FcYDi1e\",\"schema_key\":%s}}}", gvtSchemaId);

		Anoncreds.verifierVerifyProof(verifierProofRequest, new JSONObject(proofJson).toString(), schemasJson, credentialDefsJson, revocRegDefsJson, revocRegsJson).get();
	}
}
