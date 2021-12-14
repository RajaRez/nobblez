// CroneEngine_Dregz
// synths ++ synth drums ++ fx ++
//     - raja
Engine_Dregz : CroneEngine {
      classvar <maxNumSawces=16;
	    var <pg;
	    var <dg;
	    var <fxg;
	    var <mg;
	    var <sawces;
	    var <bzzpians;
	    var <dregzfx;
	    var <>fxroute;
	    var <>sidechain;
	    var <>mastering;
	    var rzr;
	    var tpd;
	    var mast;
	    var fxnum=2;
    	var gn = 1;
    	var pn = 0;
	    var sl1 = 0;
	    var atk = 0.01;
	    var rl = 0.2;
	    var md1 = 1;
	    var md2 = 0.6;

	*new { arg context, doneCallback;
		^super.new(context, doneCallback);
	}

	alloc {
		pg = ParGroup.tail(context.xg);
		dg = ParGroup.after(pg);
		fxg = ParGroup.after(dg);
		mg = ParGroup.after(fxg);
		fxroute = fxnum.do.collect({ Bus.control(context.server, 1).set(0) });
	  dregzfx = fxnum.do.collect({ Bus.audio(context.server, 2) });
	  sidechain = Bus.audio(context.server,2);
	  mastering = Bus.audio(context.server,2);
		sawces = IdentityDictionary.new;
		bzzpians = IdentityDictionary.new;
		
		SynthDef("Kik", { arg out, fxo1, fxo2, fxctr, freq=100, amp=gn, att=atk, rls=rl, sel1=sl1, pan=pn,sid;
	                      var env,env2,kenv,knv,noise1,kik,snd;
	        /*____________________________________________________
          ________________________Setupz______________________*/              
	        env = EnvGen.kr(Env.perc(att, rls, curve: -4), doneAction: 2) * amp;
	        env2 = EnvGen.kr(Env.linen(att, 0.0, 0.2, 1,\sine));
	        kenv = Env.pairs([[0.001,1],[0.013,0.0],[0.002,1],[0.001,0.0],[0.001,1],[0.013,0],[0.002,1],[0.013,0]],\lin);
	        knv = EnvGen.kr(kenv);
	        noise1 = WhiteNoise.ar * knv;
	        /*____________________________________________________
          ________________________Kick________________________*/
	                 //SOS=lopass at 770Hz with 1.5Q and gain/amp around 0.98
	        noise1 = SOS.ar(noise1,0.002417,0.004834,0.002417,1.920822,-0.93062);
          kik = SinOsc.ar(freq*env2+40)+(noise1*0.5);
          kik=((noise1*(1-sel1))+(kik*sel1))*env;
          /*____________________________________________________
          ________________________Outz:_______________________*/
          snd = Pan2.ar(kik,pan);
				  Out.ar([out,fxo1,fxo2,sid], snd);
		}).add;
		
		SynthDef("Snar", { arg out, fxo1, fxo2, amp=gn, att=atk, rls=rl, pan=pn, sid;
	                      var env,snar,snd;
	        /*____________________________________________________
          ________________________Setupz______________________*/              
	        env = EnvGen.kr(Env.perc(att, rls, curve: -4), doneAction: 2) * amp;
	        /*____________________________________________________
          ________________________Snare1______________________*/
          snar = (LFTri.ar(288) * EnvGen.ar(Env.linen(0.001, 0.0, 0.055, 0.55)))
	               +
	               SOS.ar(GrayNoise.ar,0.977,-1.954,0.977,1.952,-0.957);
          snar = snar*env;
          /*____________________________________________________
          ________________________Outz:_______________________*/
          snd = Pan2.ar(snar,pan);
				  Out.ar([out,fxo1,fxo2], snd);
		}).add;
		
		SynthDef("Snarl", { arg out, fxo1, fxo2, freq=100, amp=gn, att=atk, rls=rl, sel1=sl1, pan=pn;
	                      var env,pgen,lfs,snarr,snd;
	        /*____________________________________________________
          ________________________Setupz______________________*/              
	        env = EnvGen.kr(Env.perc(att, rls, curve: -4), doneAction: 2) * amp;
	        pgen = Env.perc(0.001,0.05,amp,-2).kr;
	        lfs = Latch.kr(PinkNoise.ar(pgen)+1*0.5,Impulse.kr(sel1));
          /*____________________________________________________
          ________________________Snare2______________________*/
          snarr = CombN.ar(PinkNoise.ar(pgen)*lfs,0.4,8/freq*lfs,rls,env);
          /*____________________________________________________
          ________________________Outz:_______________________*/
          snd = Pan2.ar(snarr,pan);
				  Out.ar([out,fxo1,fxo2], snd);
		}).add;
		
		SynthDef("Clapz", { arg out, fxo1, fxo2, amp=gn, att=atk, rls=rl, pan=pn;
	                      var env,kenv,knv,noise1,noise2,clap1,clap2,clapz,snd;
	        /*____________________________________________________
          ________________________Setupz______________________*/              
	        env = EnvGen.kr(Env.perc(att, rls, curve: -4), doneAction: 2) * amp;
	        kenv = Env.pairs([[0.001,1],[0.013,0.0],[0.002,1],[0.001,0.0],[0.001,1],[0.013,0],[0.002,1],[0.013,0]],\lin);
	        knv = EnvGen.kr(kenv);
	        noise1 = WhiteNoise.ar * knv;
	        noise2 = WhiteNoise.ar * EnvGen.kr(Env.linen(0.02, 0.0, 0.2, 1));
          /*____________________________________________________
          ________________________Clap________________________*/
          clap1 = SOS.ar(noise1,0.133743,0.0,-0.133743,1.30157,-0.72169);
	        clap2 = SOS.ar(noise2,0.110483,0,-0.110483,1.753002,-0.779836,0.9);
	        clapz = (clap1+clap2)*env;
          /*____________________________________________________
          ________________________Outz:_______________________*/
          snd = Pan2.ar(clapz,pan);
				  Out.ar([out,fxo1,fxo2], snd);
		}).add;
		
		SynthDef("Hat", { arg out, fxo1, fxo2, freq=100, amp=gn, att=atk, rls=rl, pan=pn;
		                                      
	                      var env,noic,hat1,snd;
	        /*____________________________________________________
          ________________________Setupz______________________*/              
	        env = EnvGen.kr(Env.perc(att, rls, curve: -4), doneAction: 2) * amp;
	        noic = GrayNoise.ar(Env.perc(0.001, rls, 1, -7).kr);
          /*____________________________________________________
          ________________________Hat1________________________*/
          hat1 = LPF.ar(BBandPass.ar(noic, freq, 0.2),freq)*env;
          /*____________________________________________________
          ________________________Outz:_______________________*/
          snd = Pan2.ar(hat1,pan);
				  Out.ar([out,fxo1,fxo2], snd);
		}).add;
		
		SynthDef("Het", { arg out, fxo1, fxo2, freq=100, amp=gn, att=atk, rls=rl, pan=pn;
		                                      
	                      var env,pgen,hat2,snd,side;
	        /*____________________________________________________
          ________________________Setupz______________________*/              
	        env = EnvGen.kr(Env.perc(att, rls, curve: -4), doneAction: 2) * amp;
	        pgen = Env.perc(0.001,0.05,amp,-2).kr;
          /*____________________________________________________
          ________________________Hat2________________________*/
          hat2 = LPF.ar(HPF.ar(Ringz.ar(WhiteNoise.ar(pgen),freq*(1-pgen),rls*2,env),1800),8800);
          /*____________________________________________________
          ________________________Outz:_______________________*/
          snd = Pan2.ar(hat2,pan);
				  Out.ar([out,fxo1,fxo2], snd);
		}).add;
		
		SynthDef("Bass", { arg out, freq=100, amp=gn, att=atk, rls=rl, sel1=sl1, pan=pn, mdI1=md1, mdI2=md2, sid;
	                      var env,bass,snd,side;
	        /*____________________________________________________
          ________________________Setupz______________________*/              
	        env = EnvGen.kr(Env.perc(att, rls, curve: -4), doneAction: 2) * amp;
          /*____________________________________________________
          ____________________Bass synth______________________*/
          bass = SinOsc.ar(freq, add: Pulse.ar(freq,mdI1,mdI2));
	        bass = LPF.ar(bass,freq*0.88,(bass*sel1).distort)*env;
	        /*____________________________________________________
          ________________________Outz:_______________________*/
          side = In.ar(sid,2);
          snd = Compander.ar(Pan2.ar(bass,pan),side,0.7,0.25,0.25,0.002,0.28,2);
				  Out.ar(out, snd);
		}).add;
		
		SynthDef("Puls", { arg out, fxout, freq=100, amp=gn, att=atk, rls=rl, md=#[60,61,63,65], 
		                                      sel1=sl1, pan=pn, mdI1=md1, mdI2=md2;
	                      var lead,pwm,pls,snd;
	        /*____________________________________________________
          ____________________Chordal synth___________________*/
          pwm = FSinOsc.kr(md.midicps*0.015625);
	        pls = RLPF.ar(LFPulse.ar(md.midicps,pwm.abs)*(pwm*0.5),md[0].midicps*2.2,0.4);
          lead = Env.perc(att,rls,amp,-4).ar(2)*Mix.ar(pls*0.2).softclip;
          /*____________________________________________________
          ________________________Outz:_______________________*/
          snd = Pan2.ar(lead,pan);
				  Out.ar([out,fxout], snd);
		}).add;
		
		SynthDef("Sawc", { arg out, freq=40, freq2=50, amp=gn, gate=1, pmi=pi, att=0.01, rls=0.4, pos=0;
	                      var env, osc, mdp; 
	                      var n2f=freq.midicps;  
          /*____________________________________________________
          ________________________Setupz______________________*/
	        env = EnvGen.ar(Env.adsr(att,0.3,0.7,rls),gate,doneAction: 2);
	        mdp = SinOsc.kr(freq2,0,env*20);
	        /*____________________________________________________
          ____________________Phase-Mod synth_________________*/
	        osc = PMOsc.ar(n2f,freq2,pmi,mdp,env); //pmi = phase-mod index(0 to 2pi); mdp=mod phase
	        /*____________________________________________________
          ________________________Outz:_______________________*/
	        osc = Pan2.ar(FreeVerb.ar(osc,0.6,0.4,0.8),pos);
	        Out.ar(out,LeakDC.ar(osc*amp));
    }).add;
    
    SynthDef("Bzzpian",{ arg out, freq=200, amp=gn, gate=1, 
                            ctf=888, rq=0.4, hrmw=0.1, sel1=0.5, att=0.005, rls=2.88, pos=0;
	        var pulse, blp, filter, env, verb;
	        /*____________________________________________________
          ________________________Setupz______________________*/
	        env = EnvGen.ar(Env.adsr(att,0.4,0.7,rls),gate,doneAction:2);
	        /*____________________________________________________
          ____________________Buzz-Piano synth________________*/
	        blp = Blip.ar(freq*[1,6.98],[50,150],[hrmw,0.88-hrmw]);
	        blp = Compander.ar(blp,blp,0.7,0.22,0.88);
	        pulse = (Pulse.ar(freq*[1,6.92,8.98], [hrmw,0.88-hrmw],[0.55,0.1,0.2])*sel1) + (blp * (1-sel1));
	        filter = Mix.ar(RLPF.ar(pulse,(ctf*env)+(freq*1.1),rq))*0.5;
	        verb = FreeVerb2.ar(RHPF.ar(filter,freq*0.5,0.22),RHPF.ar(filter,freq*0.7,0.44),0.4,0.65,0.8);
	        /*____________________________________________________
          ________________________Outz:_______________________*/
	        filter = Compander.ar(filter,filter+verb,0.7,1,0.88,0.005,0.2);
	        filter = (Balance2.ar(filter[0],filter[1],pos)*env);
	        Out.ar(out,filter*amp);
    }).add;
    
    SynthDef("Rezor", { arg out, freq=220, amp=0.12, rngz=0.6, fxindx=dregzfx[0].index, noff;
		
		      var freqs = Array.fill(5, { arg i; freq*((i*0.5)+1); });
		      var amps = Array.fill(5, { arg i; (1/((i+1)*3))*amp; });
		      var rings = Array.fill(5, { arg i; (1/(i+1)).pow(0.5)*rngz });
		 
		      var receive = In.ar(fxindx, 2);
		      var send = DynKlank.ar(`[freqs, amps, rings], receive);
		      send = CompanderD.ar(send,0.6,0.5,0.25,0.001,0.4);
		      Out.ar(out, send*noff);
		}).add;
		
		SynthDef("Tapdel", {arg out, time, fxindx=dregzfx[1].index, noff;

		      var receiveL = In.ar(fxindx, 1);
		      var receiveR = In.ar(fxindx+1, 1);
		      var randdec = TRand.kr(1, 20, \trigr.tr);
		      var timeL = (TIRand.kr(1,8,\trigr.tr)/2);
		      var timeR = (TIRand.kr(1,8,\trigr.tr)/2);
		      var sendL = AllpassN.ar(receiveL * noff, 4, time * timeL, randdec);
		      var sendR = AllpassN.ar(receiveR * noff, 4, time * timeR, randdec);
		      Out.ar(out, [sendL+receiveL, sendR+receiveR]);
		}).add;
		
		SynthDef("Master", {arg in=mastering, out=context.out_b, mix=0.5,thresh=0.7,below=0.5,above=0.25,att=0.004,rls=0.28;

		      var receive = In.ar(in, 2);
		      var send = Limiter.ar(CompanderD.ar(receive,thresh,below,above,att,rls),0.88,0.005);
		      Out.ar(out, send*mix + receive*(1-mix));
		}).add;
		
		this.addCommand("kik", "ffffff", { arg msg;
			var val = msg[1].midicps, gn = msg[2], sl1 = msg[3],
			pn = msg[4], atk = msg[5], rl = msg[6];
      Synth("Kik", 
      [\out,mastering,\fxo1,dregzfx[0],\fxo2,dregzfx[1],\freq,val,\amp,gn,
      \sel1,sl1,\pan,pn,\att,atk,\rls,rl,\sid,sidechain.index], 
      target:dg);
		});
		
		this.addCommand("clapz", "ffff", { arg msg;
			var val = msg[1], pn = msg[2], atk = msg[3], rl = msg[4];
      Synth("Clapz", [\out,mastering,\fxo1,dregzfx[0],\fxo2,dregzfx[1],\amp,val,\pan,pn,\att,atk,\rls,rl], target:dg);
		});
		
		this.addCommand("snar", "ffff", { arg msg;
			var val = msg[1], pn = msg[2], atk = msg[3], rl = msg[4];
      Synth("Snar", [\out,mastering,\fxo1,dregzfx[0],\fxo2,dregzfx[1],\amp,val,\pan,pn,\att,atk,\rls,rl], target:dg);
		});
		
		this.addCommand("snarl", "ffffff", { arg msg;
			var val = msg[1].midicps, gn = msg[2], sl1 = msg[3], pn = msg[4], atk = msg[5], rl = msg[6];
      Synth("Snarl", 
      [\out,mastering,\fxo1,dregzfx[0],\fxo2,dregzfx[1],\freq,val,\amp,gn,\sel1,sl1,\pan,pn,\att,atk,\rls,rl], target:dg);
		});
		
		this.addCommand("hat", "fffff", { arg msg;
			var val = msg[1].midicps, gn = msg[2], pn = msg[3], atk = msg[4], rl = msg[5];
      Synth("Hat", 
      [\out,mastering,\fxo1,dregzfx[0],\fxo2,dregzfx[1],\freq,val,\amp,gn,\pan,pn,\att,atk,\rls,rl], target:dg);
		});
		
		this.addCommand("het", "fffff", { arg msg;
			var val = msg[1].midicps, gn = msg[2], pn = msg[3], atk = msg[4], rl = msg[5];
      Synth("Het", 
      [\out,mastering,\fxo1,dregzfx[0],\fxo2,dregzfx[1],\freq,val,\amp,gn,\pan,pn,\att,atk,\rls,rl], target:dg);
		});
		
		this.addCommand("bass", "ffffffff", { arg msg;
			var val = msg[1].midicps, gn = msg[2], sl1 = msg[3],
			pn = msg[4], atk = msg[5], rl = msg[6], md1 = msg[7], md2 = msg[8];
      Synth("Bass", 
      [\out,mastering,\freq,val,\amp,gn,\sel1,sl1,\pan,pn,\att,atk,\rls,rl,\mdI1,md1,\mdI2,md2,\sid,sidechain.index], 
      target:pg);
		});
		
		this.addCommand("puls", "ffffffff", { arg msg;
			var val = [msg[1],msg[2],msg[3],msg[4]], atk = msg[5], rl = msg[6], gn = msg[7], pn = msg[8];
      Synth("Puls",[\out,mastering,\fxout,dregzfx[1],\md,val,\amp,gn,\pan,pn,\att,atk,\rls,rl], target:pg);
		});
		
		// start a new sawc
		this.addCommand("sawcitup", "iffffff", { arg msg; 
			this.addSoice(msg[1], msg[2], msg[3], msg[4], msg[5], msg[6], msg[7]);
		});

		// stop a sawc
		this.addCommand("sawcitdown", "i", { arg msg;
			this.removeSoice(msg[1]);
		});
		
		// start a new bzzp
		this.addCommand("bzzpup", "iffffffff", { arg msg;
			this.addBoice(msg[1], msg[2], msg[3], msg[4], msg[5], msg[6], msg[7], msg[8], msg[9]);
		});


		// stop a bzzp
		this.addCommand("bzzpdown", "i", { arg msg;
			this.removeBoice(msg[1]);
		});
		
		this.addCommand("rzr", "ifff", { arg msg;
			var val = msg[1].midicps, gn = msg[2], sl1 = msg[3], md1 = msg[4];
      rzr = Synth("Rezor", 
      [\out,dregzfx[1],\freq,val,\amp,gn,\rngz,sl1,\fxindx,dregzfx[md1],\noff,fxroute[0].asMap], 
      target:fxg);
		});
		
		this.addCommand("rzset", "iff", { arg msg;
			var val = msg[1].midicps, gn = msg[2], sl1 = msg[3];
      rzr.set(\freq,val,\amp,gn,\rngz,sl1);
		});
		
		this.addCommand("tpd", "fff", { arg msg;
			var val = msg[1], sl1 = msg[2], md1 = msg[3];
      tpd = Synth("Tapdel", 
      [\out,mastering,\time,val,\trigr,sl1,\fxindx,dregzfx[md1],\noff,fxroute[1].asMap], 
      target:fxg, addAction: \addToTail);
		});
		
		this.addCommand("tpdset", "ff", { arg msg;
			var val = msg[1], sl1 = msg[2];
      tpd.set(\time,val,\trigr,sl1);
		});
		
		this.addCommand("master", "", { arg msg;
      mast = Synth("Master", 
      [\in,mastering,\out,context.out_b,\mix,0.5,\thresh,0.88,\below,0.7,\above,0.6667,\att,0.004,\rls,0.4], 
      target: mg);
		});
		
		this.addCommand("mastset", "ffffff", { arg msg;
			var val = msg[1], sl1 = msg[2], md1 = msg[3], md2 = msg[4], atk = msg[5], rl = msg[6];
      mast.set(\mix,val,\thresh,sl1,\below,md1,\above,md2,\att,atk,\rls,rl);
		});
		
		this.addCommand("fxrtrz", "i", { arg msg; fxroute[0].set(msg[1]); });
		
		this.addCommand("fxrtpd", "i", { arg msg; fxroute[1].set(msg[1]); });
		
		// free all synths
		this.addCommand("darknez", "", {
			pg.set(\gate, 0);
			dg.set(\gate, 0);
			sawces.clear;
			bzzpians.clear;
			this.free;
		});
	}
	
	addSoice { arg id, fr2, am, pmii, at, rl, ps;
		var params = Array.with(\out,mastering, \freq, id, \freq2, fr2, \amp, am, \pmi, pmii, \att, at, \rls, rl, \pos, ps);
		var numSawces = sawces.size;

		if(numSawces < maxNumSawces, 
		{ if((sawces[id].notNil), 
		    { if(sawces[id].isRunning==true){ sawces[id].set(\rls, 0.001); sawces[id].set(\gate, 0); } },
			  { sawces.add(id -> Synth.new("Sawc", params, target:pg));
			    NodeWatcher.register(sawces[id]);
			    sawces[id].onFree({ sawces.removeAt(id); }); 
			  });
		});
	}
	
	removeSoice { arg id; sawces[id].set(\gate, 0); }
	
	addBoice { arg id, am, cf, req, hrm, se, at, rl, ps; 
		var params = Array.with(\out,mastering, 
		              \freq, id.midicps, \amp, am, \ctf, cf, \rq, req, \hrmw, hrm, \sel1, se, \att, at, \rls, rl, \pos, ps);
		var numBzzps = bzzpians.size;

		if(numBzzps < maxNumSawces, 
		{ if((bzzpians[id].notNil), 
		    { if(bzzpians[id].isRunning==true){ bzzpians[id].set(\rls, 0.001); bzzpians[id].set(\gate, 0); } },
			  { bzzpians.add(id -> Synth.new("Bzzpian", params, target:pg));
			    NodeWatcher.register(bzzpians[id]);
			    bzzpians[id].onFree({ bzzpians.removeAt(id); }); 
			  });
		});
	}
	
	removeBoice { arg id; bzzpians[id].set(\gate, 0); }
	
	free { pg.free; dg.free; fxg.free; mg.free; dregzfx.free; fxroute.free; sidechain.free; mastering.free; }
}