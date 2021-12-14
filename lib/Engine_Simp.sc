//Engine_Simp
//simplest crone engine examp (according to raja)
Engine_Simp : CroneEngine { 
                var pg;
                
                *new { arg context, doneCallback;
                ^super.new(context, doneCallback);
                }
                
                alloc {
                
                  pg = ParGroup.tail(context.xg);
                
                  SynthDef(\simpy, { arg out=0, freq=220, amp=0.5, dur=0.4, pan=0;
                
                        var env, osc, sig;
                        env = Env.perc(0.002,dur,amp).kr(2);
                        osc = FSinOsc.ar(freq);
                        sig = osc * env;
                        Out.ar(out, Pan2.ar(sig, pan));
                  }).add;
                
                  this.addCommand("doit", "f", { arg msg;
                                  var frq = msg[1].midicps;
                                  Synth(\simpy, [\out, context.out_b, \freq, frq], target: pg);
                  });
                
                }
}