# 번개 스터디 (2025/3/5)

## 목차

* [1. 가상화 솔루션 한방 정리](#1-가상화-솔루션-한방-정리)
* [2. IP 주소 서브넷 마스크 계산법](#2-ip-주소-서브넷-마스크-계산법)
* [3. 타임존의 비밀 GMT냐 UTC냐](#3-타임존의-비밀-gmt냐-utc냐)

## 1. 가상화 솔루션 한방 정리

### 주요 가상화 솔루션 제품 목록

| Vendor       | Hypervisor                       | Type | VDisk Formats    | Supported Host            | Dev Supporter     |
|--------------|----------------------------------|------|------------------|---------------------------|-------------------|
| Broadcom     | VMware vSphere Hypervisor (ESXi) | 1    | VMDK             |                           |                   |
|              | VMware ESX (deprecated)          | 2    |                  |                           |                   |
|              | VMware Workstation/Fusion/Player | 2    |                  | Windows/Mac/Linux         |                   |
| Microsoft    | Hyper-V Server                   | 1    | VHD, VHDX, VHDS  |                           |                   |
|              | Hyper-V Service (vmms)           | 2    |                  | Windows Pro/Server        |                   |
|              | VirtualPC (deprecated)           | 2    | VHD              | Windows (old)             |                   |
| Oracle       | VirtualBox                       | 2    | VDI              | Windows/Mac/Linux/Solaris |                   |
| (opensource) | KVM (with QEMU)                  | 1.5? | QCOW2, RAW ...   | Linux                     | RedHat ...        |
| Proxmox      | Proxmox VE (KVM vm server)       | 1.5? |                  |                           |                   |
| Xen Project  | Xen Hypervisor                   | P    |                  | Linux                     | Intel, Citrix ... | 
|              | XCP-ng (Xen one-stop solution)   | P    |                  |                           |                   |
| Pararells    | Parallels Desktop for Mac        | 2    | HDD              | Mac                       |                   |

### Hypervisor 타입

* Type 1: VM이 호스트 OS를 거치지 않고 하드웨어에 직접 접근 (주로 서버, OS처럼 설치함)
  * Hyper-V Server: 완벽한 Type 1이고 호스트의 관리기능이나 GUI 조차도 내부적으로는 하나의 VM임
  * VMware ESXi: 호스트가 리눅스의 일부 기능을 제공하지만 범용이라고 하기에는 제한이 많이 됨
* Type 1.5: KVM의 경우 Type 1이라 할 수도 있지만, 관점에 따라 Type 2라고 할수도 있음
  * VM은 호스트의 커널에서 동작하므로 Type 1임
  * 호스트 OS는 범용 OS로 사용 가능하므로 Type 2라 볼 수도 있음 (다른 프로세스 입장에서는 VM이 하나의 프로세스로 보임)
* Type 2: VM이 호스트 OS를 거쳐서 하드웨어에 접근해야함 (그냥 하나의 응용프로그램처럼 동작)
  * VMware ESX: 가상화 전용 서버 제품이지만 가상화 구조상으로는 Type 2임 (ESXi의 전버전)
* Para-Virtualization(반 가상화): Type 2처럼 호스트 OS를 거쳐서 실행되나, 게스트가 하드웨어에 직접 접근할 수 있도록 허용됨. \
  따라서 게스트의 커널이 Xen을 지원해야 하므로 수정이 필요했으나, 요즘은 인텔/AMD의 하드웨어 가상화 기술을 대부분 OS가 지원하므로 그냥 잘 동작함

### QEMU
* 원래 가상화(virtualization)이 아니라 에뮬레이션(emulation) 솔루션
* KVM이 가상머신 관리 도구로 QEMU의 기능들을 차용해서 사용하는 것임
* KVM없이 순수하게 에뮬레이션된 가상머신을 돌릴 수도 있음

### VDI(Virtual Desktop Infrastructure)

* 회사 등에서 데스크톱 pc를 가상 머신으로 제공하는 솔루션

## 2. IP 주소 서브넷 마스크 계산법

### IP 주소 클래스

| Class   | Binary IP Address (4 byte)              | Network                 | Host           | Max hosts  | Desc      |
|---------|-----------------------------------------|-------------------------|----------------|------------|-----------|
| A Class | **0nnnnnnn** hhhhhhhh hhhhhhhh hhhhhhhh | 0 ~ 127                 | 0 ~ 16,777,215 | 16,777,214 |           |
| B Class | **10nnnnnn nnnnnnnn** hhhhhhhh hhhhhhhh | 128.0 ~ 191.255         | 0 ~ 65,535     | 65,534     |           |
| C Class | **110nnnnn nnnnnnnn nnnnnnnn** hhhhhhhh | 192.0.0 ~ 223.255.255   | 0 ~ 255        | 254        |           |

#### 특수 IP 대역

| Class   | Binary IP Address (4 byte)              | Network                 | Desc      |
|---------|-----------------------------------------|-------------------------|-----------|
| A Class | **00001010** hhhhhhhh hhhhhhhh hhhhhhhh | 10                      | Private   |
| B Class | **10101100 0001nnnn** hhhhhhhh hhhhhhhh | 172.16 ~ 172.31         | Private   |
| C Class | **11000000 10101000 nnnnnnnn** hhhhhhhh | 192.168.0 ~ 192.168.255 | Private   |
| B Class | **10101000 11111110** hhhhhhhh hhhhhhhh | 169.254                 | APIPA     |
| D Class | **1110**hhhh hhhhhhhh hhhhhhhh hhhhhhhh | 224 ~ 239               | Multicast |
| E Class | **1111**hhhh hhhhhhhh hhhhhhhh hhhhhhhh | 240 ~ 255               | Reserved  |

* APIPA(Automatic Private IP Addressing): DHCP 주소 요청이 실패했을 때 자동 사용됨

### CIDR (Classless Inter-Domain Routing)

IP 주소가 부족해지자 사용하지 않는 IP를 회수하여 클래스와 무관하게 막 할당함. (누가? NIC가)

```
$ curl ipconfig.io
175.124.56.73
$ whois 175.124.56.73
% IANA WHOIS server
% for more information on IANA, visit http://www.iana.org
% This query returned 1 object

refer:        whois.apnic.net

inetnum:      175.0.0.0 - 175.255.255.255
organisation: APNIC
status:       ALLOCATED

whois:        whois.apnic.net

changed:      2009-08
source:       IANA

# whois.apnic.net

% [whois.apnic.net]
% Whois data copyright terms    http://www.apnic.net/db/dbcopyright.html

% Information related to '175.112.0.0 - 175.127.255.255'

% Abuse contact for '175.112.0.0 - 175.127.255.255' is 'irt@nic.or.kr'

inetnum:        175.112.0.0 - 175.127.255.255
netname:        broadNnet
descr:          SK Broadband Co Ltd
admin-c:        IM670-AP
tech-c:         IM670-AP
country:        KR
status:         ALLOCATED PORTABLE
mnt-by:         MNT-KRNIC-AP
mnt-irt:        IRT-KRNIC-KR
last-modified:  2017-02-03T00:38:20Z
source:         APNIC

...(중략)...

% Information related to '175.112.0.0 - 175.127.255.255'

...(중략)...

% This query was served by the APNIC Whois Service version 1.88.25 (WHOIS-JP3)
```

### CIDR 표기법

> 192.168.1.1/24

* IP 주소: 192.168.1.1
* Prefix: 2진수로 바꿨을때 앞에서 1로 채울 갯수 (서브넷마스크로 변환 가능)
  * Subnet Mask: 255.255.255.0
  * Network Address: 192.168.1.1 & 255.255.255.0 => 192.168.1.0
  * Broadcast Address: 192.168.1.1 | ~255.255.255.0 => 192.168.1.255

### 서브넷 계산 표

| Prefix     | Subnet mask     | 2^n | Hosts | Subnets                               |
|------------|-----------------|-----|-------|---------------------------------------|
| 1.2.3.0/24 | 255.255.255.0   | 256 |   254 | 1.2.3.0/24                            |
| 1.2.3.0/25 | 255.255.255.128 | 128 |   126 | 1.2.3.{0,128}/25                      |
| 1.2.3.0/26 | 255.255.255.192 |  64 |    62 | 1.2.3.{0,64,128,192}/26               |
| 1.2.3.0/27 | 255.255.255.224 |  32 |    30 | 1.2.3.{0,32,64,96,128,160,192,224}/27 |
| 1.2.3.0/28 | 255.255.255.240 |  16 |    14 |                                       |
| 1.2.3.0/29 | 255.255.255.248 |   8 |     6 |                                       |
| 1.2.3.0/30 | 255.255.255.252 |   4 |     2 |                                       |
| 1.2.3.0/31 | 255.255.255.376 |   2 |       | (무의미함)                             |
| 1.2.3.0/32 | 255.255.255.255 |   1 |       | (개별호스트)                           |

### 서브넷 계산 실전 문제

#### 1. 다음 IP 주소의 네트워크 주소, 서브넷마스크, 브로드캐스트 주소를 기술하시오.

> 1.2.3.78/30

* Network: 
* Netmask: 
* Broadcast: 

<details>
<summary>스포일러 주의</summary>

#### 해설: 2진수 변환 테이블

|     | bits     | division | quotient | remainder |
|-----|----------|----------|----------|-----------|
| 128 | 10000000 | 78 ÷ 128 | 0        | 78        |
| 64  | 01000000 | 78 ÷ 64  | 1        | 14        |
| 32  | 00100000 | 14 ÷ 32  | 0        | 14        |
| 16  | 00010000 | 14 ÷ 16  | 0        | 14        |
| 8   | 00001000 | 14 ÷ 8   | 1        | 6         |
| 4   | 00000100 | 6  ÷ 4   | 1        | 2         |
| -   | -        | -        | -        | -         |
| 2   | 00000010 | 2  ÷ 2   | 1        | 0         |
| 1   | 00000001 |          | 0        |           |

> 정답:
> * Network: **1.2.3.76**
> * Netmask: **255.255.255.252**
> * Broadcast: **1.2.3.79**
</details>

#### 2. 네트워크 주소가 172.16.0.0 서브넷 마스크가 255.255.254.0 인 경우 네트워크에 사용 가능한 최대 호스트 수는 몇개인가?

1. 254개
2. 510개
3. 1022개
4. 쫌 많은 듯

<details>
<summary>스포일러 주의</summary>

#### 해설

* netmask prefix: 172.16.0.0/23
* 가능한 호스트 172.16.0.1 ~ 172.16.1.254 까지 가능하다 => 510개 인듯 한데
* 스위치 설정에 따라서는 172.16.0.0/23 또는 172.16.0.0/24를 사용 못할 수 있음 (subnet-zero issue)

> **정답: 2 || 4**

</details>

### 참고 문헌

* https://yonghyunlee.gitlab.io/temp_post/network-2/
* https://limkydev.tistory.com/168
* https://m.blog.naver.com/newyks/220268604200

## 3. 타임존의 비밀 GMT냐 UTC냐

> 정답: UTC가 맞음

### GMT

* [지방평균시(Local mean time)](https://en.wikipedia.org/wiki/Local_mean_time): 해당 지역의 해가 뜨고 지는 것을 기준으로 정한 시간 체계. 지역마다 달라진다. (경도 1도당 4분 차이남)
* [GMT(Greenwich mean time)](https://en.wikipedia.org/wiki/Greenwich_Mean_Time): 런던의 왕립 천문대(Royal Observatory) 기준의 지방시
  * 그리니치 천문대 관측자료를 기반으로한 측정방법이 선원들에게 널리 퍼지면서 항해할때 기준 시간으로 활용됨
  * 1924년 라디오로 시각정보를 방송 시작 (시간 동기화용)
  * 1925년 자정을 0시로 하는 기준이 정립됨: 1924년 12월 31일 5시 GMAT(그리니치평균천문시) == 1925년 1월 1일 0시 GMT
* 1925년 2월 5일 ~ 1972년 1월 1일까지 세계 표준시로 사용되었고 이후 [UTC(Coordinated Universal Time, 세계협정시)](https://en.wikipedia.org/wiki/Coordinated_Universal_Time)로 대체됨

### GMT의 문제점

* GMT 시절의 1초의 기준은 평균태양일의 1/86400 으로 정의되는데, 문제는 지구의 자전주기가 항상 일정하지 않다
* 1초의 불균일성을 보정한 시간이 [세계시(Universal Time)](https://en.wikipedia.org/wiki/Universal_Time)
  * UT0 - 평균태양일 기준 시간 + 지구가 기울어진 상태로 타원형 궤도를 공전하는 효과를 보정
  * UT1 - UT0 + 지구의 극운동 효과를 보정
  * UT2 - UT1 + 계절에 따른 지구 자전 속도 효과 보정
* 1967년 1초의 정의를 세슘원자(Cs)가 9,192,631,770 번 진동하는데 걸리는 시간으로 정의됨 (원자 시계)

### UTC

* 1972년 1월 1일 부터 시행된 국제 표준시
* 1970년 1월 1일 0시 정각을 0초로 하고 이후 경과된 밀리초로 시간을 정함
* 현재 UTC는 원자시계 기준으로 정해지는데 이는 실제 지구의 자전/공전과 무관하다!
  * 따라서 차이점이 누적되면 해뜨는 시간, 계절 변화와 안맞아 질 수 있으므로 적당한 때에 [윤초(leap second)](https://en.wikipedia.org/wiki/Leap_second)를 삽입함
    * 1972년 이후 지금까지 총 37초가 추가됨
    * 따라서 OS 업데이트시에 시스템 캘린더 데이터가 업데이트가 되는 경우가 있음

### GMT 시절의 타임존(Military Time Zone)

* 경도 15도당 1시간씩 차이를 두며 전 지구(360도)를 15도씩 나누면 24개 타임존이 됨
* 24개 타임존에 순서대로 A ~ Z (J는 제외)로 배정하고 포네틱 알파벳명으로 이름을 붙여서 사용함 ([NATO 타임존](https://en.wikipedia.org/wiki/Military_time_zone)으로 남아있음)
  * Alfa Time Zone: GMT+01:00
  * Brabo Time Zone: GMT+02:00
  * ...
  * India Time Zone: GMT+09:00
  * ...
  * Mike Time Zone: GMT+12:00
  * ...
  * Yankee Time Zone: GMT-12:00
  * Zulu: GMT+00:00
* 시간 표시할때 타임존을 알파벳 한글자를 붙여서 표현했음: 한국 시간 02:00 => 0200I, GMT 15:00 => 1500Z
  * 아직도 프로그램에 따라 시간 포맷을 사용할때 'Z'를 GMT/UTC 로 해석하는 경우가 있음

### UTC 기준 타임존

* GMT대신 UTC를 기준으로 사용
* UTC Offset: UTC 기준 몇시간 차이나냐를 UTC+09:00 같은 형태로 표기
* 국가별 타임존 이름: 각 국가별로 별도로 지정되어 있음
* OS의 타임존 설정의 경우 도시명으로 설정가능

> * UTC+09:00 == KST == JST == PYT == Asia/Seoul
> * UTC ≠ Europe/London
> * UTC ≒ Africa/Dakar

### Linux에서의 타임존

타임존 설정은 2가지로 가능함

* `TZ` 환경변수: 프로그램에 적용될 타임존을 지정함
* [`/etc/localtime`](https://man7.org/linux/man-pages/man5/localtime.5.html): 시스템의 타임존 설정

```
$ ls -l /etc/localtime
lrwxrwxrwx 1 root root 30 Mar  5 17:06 /etc/localtime -> /usr/share/zoneinfo/Asia/Seoul
$ file /usr/share/zoneinfo/Asia/Seoul
/usr/share/zoneinfo/Asia/Seoul: timezone data (fat), version 2, no gmt time flags, no std time flags, no leap seconds, 29 transition times, 7 local time types, 16 abbreviation chars
$ date
Wed Mar  5 17:07:56 KST 2025
$ TZ=UTC date
Wed Mar  5 08:08:03 UTC 2025
$ timedatectl
               Local time: Wed 2025-03-05 17:09:00 KST
           Universal time: Wed 2025-03-05 08:09:00 UTC
                 RTC time: Wed 2025-03-05 08:09:00
                Time zone: Asia/Seoul (KST, +0900)
System clock synchronized: yes
              NTP service: active
          RTC in local TZ: no
$
```

### 참고문헌

* http://time.ewha.or.kr/world_time/utc.html
* https://junhyunny.github.io/information/gmt-and-utc/
