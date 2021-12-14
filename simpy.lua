engine.name = 'Simp'
notab = {0,1,4,5,7,8,10}

function init()
  clock.run(randomWeirdness)
end

function randomWeirdness()
  local freq
  while true do
    clock.sync(0.25)
    freq = notab[math.random(1,7)]+60
    engine.doit(freq)
  end
end