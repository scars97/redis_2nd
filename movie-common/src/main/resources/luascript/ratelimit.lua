local key = KEYS[1] -- 제한 키
local limitCount = tonumber(ARGV[1]) -- 요청 허용 횟수
local ttl = tonumber(ARGV[2]) -- ttl 값
local current = redis.call('GET', key)

if current then
	current = tonumber(current)
	if current >= limitCount then
	    return 0
    end
else
    redis.call('SET', key, 0, 'EX', ttl)
end

redis.call('INCR', key)
return 1