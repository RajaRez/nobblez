--
-- ...reach out 
--          with your feelings...
--(the knobs control something..
-- to do with rhythm..
--the keys control something..
-- to do with fx)
-- by raja(TheResidentAlien)

engine.name = 'Dregz'

dest = {"192.168.1.65",7007}
ltt = require("lattice")
bdiv = 1/96
sdiv = 0
swflag = 0
notetab = {65,67,68,70,72,73,75,77}

cflag = 0
lflag = 0
bflag = 0
sflag = 0
longcount = 1
count = 1
note1 = 1
note2 = 1
note3 = 1 
note4 = 1

comz = {
  bass = function(n1)
    engine.bass(
    n1-48,  --freq 
    math.random(2,5)*0.15,   --amp
    math.random(0,2)*0.25,  --sl1
    0,                      --pn
    math.random(2,40)*0.001,--atk
    math.random(10,20)*0.04, --rl
    math.random(0,2)*0.25 + 0.2,  --md1
    math.random(0,4)*0.125 + 0.2  --md2
  )
  osc.send(dest, "/bass", {"bang"})
  end,
  lkk = function()
    engine.kik(
    (math.random(1,10)*0.2*15),  --freq 
    0.4,  --amp
    1,  --sl1
    0, --pn
    0.001,--atk
    1  --rl
  )
  osc.send(dest, "/lkk", {"bang"})
  end,
  skk = function()
    engine.kik(
    (math.random(1,10)*0.2*15),  --freq 
    0.4,  --amp
    math.random(1,10)*0.2,  --sl1
    0, --pn
    0.001,--atk
    0.24  --rl
  )
  osc.send(dest, "/skk", {"bang"})
  end,
  clp = function(p)
    engine.clapz(
    p,   --amp
    math.random(-2,2)*0.2,  --pn
    0.001,--atk
    math.random(1,10)*0.008 --rl
  )
  osc.send(dest, "/clp", {"bang"})
  end,
  sn1 = function()
    engine.snar(
    math.random(2,4)*0.1,   --amp
    math.random(-2,2)*0.1,  --pn
    0.001,--atk
    math.random(4,10)*0.008       --rl
  )
  osc.send(dest, "/sna", {"bang"})
  end,
  sn2 = function()
    engine.snarl(
    (math.random(10,15)*0.2*25),  --freq 
    math.random(4,8)*0.2,   --amp
    math.random(1,10)*0.002,  --sl1
    math.random(-2,2)*0.1,    --pn
    0.001,--atk
    math.random(4,10)*0.07       --rl
  )
  osc.send(dest, "/snr", {"bang"})
  end,
  ht1 = function()
    engine.hat(
    (math.random(10,15)*0.2*35),  --freq 
    math.random(4,8)*0.07,   --amp
    math.random(-4,4)*0.25,  --pn
    0.001,                   --atk
    math.random(4,10)*0.007  --rl
  )
  osc.send(dest, "/hat", {"bang"})
  end,
  chord = function(n1,n2,n3,n4)
    engine.puls(
    n1,  --midi notes 
    n2,
    n3,
    n4,
    math.random(2,40)*0.01,--atk
    math.random(10,20)*0.04,--rl
    math.random(1,4)*0.15,  --gain
    math.random(-4,4)*0.25  --pan
  )
  osc.send(dest, "/chr", {"bang"})
  end,
  ht2 = function()
    engine.het(
    (math.random(10,15)*6), --freq 
    math.random(4,8)*0.04,  --amp
    math.random(-8,8)*0.125,--pn
    0.001,                  --atk
    math.random(4,10)*0.004 --rl
  )
  osc.send(dest, "/het", {"bang"})
  end,
  ht3 = function()
    engine.het(
    (math.random(10,15)*22),--freq 
    math.random(4,8)*0.02,  --amp
    math.random(-5,5)*0.2,  --pn
    0.001,                  --atk
    math.random(4,10)*0.004 --rl
  )
  osc.send(dest, "/hit", {"bang"})
  end,
  bzz = function(n1,n2)
      engine.bzzpup(
      n1,  --freq 
      n2,   --amp
      math.random(1,10)*800,  --cutoff
      1.1,                      --rq
      math.random(1,7)*0.1, --harmonic width(?)
      math.random(0,10)*0.1, --sel1 = crossfade value
      math.random(0,20)*0.01, --atk
      math.random(4,10)*0.01, --rl
      math.random(-4,4)*0.25 --pos
      )
      osc.send(dest, "/bzz", {"bang"})
  end,
  swc = function(n1,n2)
      engine.sawcitup(
      n1,  --freq(midinote)
      math.random(5,550)*11, --freq2
      n2,                     --amp
      math.random(1,10)*0.1,  --pmi
      math.random(0,20)*0.01, --atk
      math.random(4,10)*0.01,  --rl
      math.random(-4,4)*0.25 --pos
      )
      osc.send(dest, "/swc", {"bang"})
  end
}

lkk = {
  1,0,0,0,
  0,0,0,0,
  0,0,0,0,
  0,0,0,0,
  0,0,0,0
}

skk = {
  0,0,0,0,
  0,0,1,1,
  0,1,1,0,
  0,0,0,0,
  0,0,0,0
}

clp = {
  0,0,0,1,
  1,0,1,0,
  0,0,1,1,
  1,0,1,0,
}

sn1 = {
  0,0,0,0,
  1,0,0,0,
  0,0,0,0,
  1,0,0,0,
}

sn2 = {
  0,0,0,0,
  0,0,0,0,
  0,0,0,0,
  1,1,0,0,
}

function init()
  -- set sensitivity of the encoders
  norns.enc.sens(1,1)
  norns.enc.sens(2,1)
  norns.enc.sens(3,1)
  -- default lattice usage, with no arguments
  latt = ltt:new()
  latt.auto = false
  patt1 = latt:new_pattern{
    action = 
    function(t)
      local n1,n2
      count = (((t/24)-1)%16)+1
      longcount = t%3072
      if lkk[count]>0 then comz.lkk() end
      if t>4000 then if skk[count]>0 then comz.skk() end end
      if t>6000 then if math.random(-2,1)>-1 then comz.ht1() else comz.ht3() end end
      if t>8000 then
      if ((((t/24)-1)%(math.random(1,4)*4))+1 == 1) then 
      comz.chord(notetab[note1],notetab[note2],notetab[note3],notetab[note4]) 
      end
      end
      if t>10000 then
      if ((((t/24)-1)%(math.random(1,4)*4))+1 == 1) then comz.bass(notetab[note1]) end
      end
      if bflag>1 then 
        n1 = notetab[note2]*util.round(((1/math.random(2,3))*1.5),1)
        comz.bzz(n1,math.random(1,2)*0.02) clock.run(b_off,n1)
        else 
          if t>2000 then if sn1[count]>0 then comz.sn1() end end
      end
      if sflag>1 then 
        n2 = notetab[note3]*util.round(((1/math.random(3,4))*1.5),1)
        comz.swc(n2,math.random(1,2)*0.05) clock.run(s_off,n2)
        else
          if t>3000 then if sn2[count]>0 then comz.sn2() end end
          if clp[count]>0 then comz.clp(cflag*0.05) end
      end
    end,
    division = 1/16
  }
  
  patt2 = latt:new_pattern{
    action = 
    function(t)
      local dflag, n
      dflag = math.random(-4,1) 
      if dflag>0 then 
        patt2:set_division(1/32) 
        n = notetab[math.random(1,8)]*0.33333
        comz.swc(n,math.random(1,2)*0.02) clock.run(s_off,n)
      elseif dflag<-3 then patt2:set_division(1/16) 
      end
      if t>12000 then comz:ht2() end
    end,
    division = 1/32
  }
  
  patt3 = latt:new_pattern{
    action = 
    function(t)
      cflag = math.random(10,20)
      note1 = math.random(1,8)
      note2 = math.random(1,8)
      note3 = math.random(1,8)
      note4 = math.random(1,8)
      if longcount==0 then lflag = 1-lflag end
      if lflag>0 then bflag = math.random(1,2) sflag = math.random(1,2) end
    end,
    division = 1/1
  }

  -- start the lattice
  latt:start()

  -- demo stuff
  draw_dirt = true
  drawck = clock.run(screen_clock)
  lattck = clock.run(latt_clock, bdiv)
  engine.rzr(55,0.12,0.4,0)
  engine.tpd(clock.get_beat_sec()*0.5,1,1)
  engine.master()
end

function key(k, z)
  local oct = math.random(-1,1)*12
  local wut = notetab[math.random(8)]-oct
  if k == 1 then
    engine.darknez()
  elseif k == 2 then
    engine.rzset(wut,wut*0.001,wut*0.01)
    engine.fxrtrz(z)
  elseif k == 3 then
    engine.tpdset(clock.get_beat_sec()*0.5,z)
    engine.fxrtpd(z)
  end
  if z == 1 then osc.send(dest, "/x", {z}) end
end

function enc(e, d)
  if e==1 then
    params:set("clock_tempo", params:get("clock_tempo") + d)
  elseif e==2 then
    bdiv = util.clamp(bdiv + (d*1/768),1/768,192)
  elseif e==3 then
    sdiv = util.clamp(sdiv + d,0,24)
  end
  draw_dirt = true
end

function latt_clock(bdv)
  local count=0
  while true do
    count=(count+1)%24
    clock.sync(bdiv,0.015625*swflag)
    latt:pulse()
    if count==sdiv then swflag = 1-swflag end
  end
end

function b_off(note)
  clock.sleep(math.random(1,2))
  engine.bzzpdown(note)
end

function s_off(note)
  clock.sleep(math.random(1,2))
  engine.sawcitdown(note)
end

function cleanup()
  latt:destroy()
  engine.darknez()
end

-- screen stuff

function screen_clock()
  while true do
    clock.sleep(0.25)
    if draw_dirt then
      redraw()
      draw_dirt = false
    end
  end
end

function redraw()
  screen.clear()
  screen.level(15)
  screen.aa(0)
  screen.font_size(8)
  screen.font_face(44)
  screen.move(1, 8)
  screen.text(params:get("clock_tempo") .. " BPM")
  screen.move(40, 8)
  screen.text(util.round(bdiv*96,0.1))
  screen.move(55,8)
  screen.text(" BtDiv")
  screen.move(92, 8)
  screen.text(sdiv)
  screen.move(100, 8)
  screen.text(" Swing")
  screen.font_face(0)
  screen.font_size(8)
  screen.move(0, 30)
  screen.text("...reach out")
  screen.move(0, 38)
  screen.text("         with your feelings...")
  screen.update()
end