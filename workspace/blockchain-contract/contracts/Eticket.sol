// SPDX-License-Identifier: MIT
pragma solidity ^0.8.19;

import '@openzeppelin/contracts/utils/Strings.sol';
import '@openzeppelin/contracts/access/Ownable.sol';
import '@openzeppelin/contracts/token/ERC721/ERC721.sol';
import '@openzeppelin/contracts/token/ERC721/extensions/ERC721Enumerable.sol';

contract Eticket is Ownable, ERC721Enumerable {
    struct Ticket {
        bool isUsed;
    }

    struct PerformanceSchedule {
        mapping(uint32 => uint) ticketIndices;
        Ticket[] tickets;
        uint ticketExpirationTime;
        string ticketDefaultContentBaseUrl;
        string ticketSpecialContentBaseUrl;
        bool isScheduled;
    }

    mapping(uint32 => PerformanceSchedule) _performanceSchedules;

    constructor(string memory name_, string memory symbol_) Ownable() ERC721(name_, symbol_) {}

    function _requireScheduled(uint32 performanceScheduleId) internal view {
        require(
            _performanceSchedules[performanceScheduleId].isScheduled,
            '_assertPerformanceScheduled: performance is not scheduled'
        );
    }

    function makeTokenId(uint32 performanceScheduleId, uint32 seatId) public pure returns (uint) {
        return (((uint)(performanceScheduleId)) << 32) | seatId;
    }

    function obtainPerformanceScheduleId(uint tokenId) public pure returns (uint32) {
        return (uint32)((tokenId >> 32) & 0xffffffff);
    }

    function obtainSeatId(uint tokenId) public pure returns (uint32) {
        return (uint32)(tokenId & 0xffffffff);
    }

    // @dev 공연 스케줄이 등록되어 있는지 확인하는 함수
    function isPerformanceScheduled(uint32 performanceScheduleId) external view returns (bool) {
        return _performanceSchedules[performanceScheduleId].isScheduled;
    }

    // @dev 공연 스케줄을 등록하는 함수
    function schedulePerformance(
        uint32 performanceScheduleId,
        uint ticketExpirationTime,
        string memory ticketDefaultContentUrl,
        string memory ticketSpecialContentBaseUrl
    ) external onlyOwner {
        PerformanceSchedule storage schedule = _performanceSchedules[performanceScheduleId];
        require(!schedule.isScheduled, 'schedulePerformance(): performance already scheduled.');

        schedule.isScheduled = true;
        schedule.ticketExpirationTime = ticketExpirationTime;
        schedule.ticketDefaultContentBaseUrl = ticketDefaultContentUrl;
        schedule.ticketSpecialContentBaseUrl = ticketSpecialContentBaseUrl;
    }

    function _unsafe_ticketRef(uint32 performanceScheduleId, uint32 seatId) internal view returns (Ticket storage) {
        PerformanceSchedule storage schedule = _performanceSchedules[performanceScheduleId];
        uint ticketIndex = schedule.ticketIndices[seatId];
        return schedule.tickets[ticketIndex - 1];
    }

    function _unsafe_ticketExpirationTimeOf(uint32 performanceScheduleId) private view returns (uint) {
        PerformanceSchedule storage schedule = _performanceSchedules[performanceScheduleId];
        return schedule.ticketExpirationTime;
    }

    function _unsafe_isTicketExpired(uint tokenId) internal view returns (bool) {
        uint32 performanceScheduleId = obtainPerformanceScheduleId(tokenId);
        uint ticketExpirationTime = _unsafe_ticketExpirationTimeOf(performanceScheduleId);
        return ticketExpirationTime <= block.timestamp;
    }

    // @dev 특정 공연 스케줄에 해당하는 티켓의 만료 시간을 얻어오는 함수
    function ticketExpirationTimeOf(uint32 performanceScheduleId) external view returns (uint) {
        _requireScheduled(performanceScheduleId);

        return _unsafe_ticketExpirationTimeOf(performanceScheduleId);
    }

    function ticketOwnerOf(uint32 performanceScheduleId, uint32 seatId) external view returns (address) {
        uint tokenId = makeTokenId(performanceScheduleId, seatId);
        _requireMinted(tokenId);

        return ownerOf(tokenId);
    }

    function isTicketUsed(uint32 performanceScheduleId, uint32 seatId) external view returns (bool) {
        _requireMinted(makeTokenId(performanceScheduleId, seatId));

        return _unsafe_ticketRef(performanceScheduleId, seatId).isUsed;
    }

    function markTicketAsUsed(uint32 performanceScheduleId, uint32 seatId) external onlyOwner {
        _requireScheduled(performanceScheduleId);
        _requireMinted(makeTokenId(performanceScheduleId, seatId));

        Ticket storage ticket = _unsafe_ticketRef(performanceScheduleId, seatId);
        ticket.isUsed = true;
    }

    // @dev 특정 공연 스케줄의 좌석의 티켓이 발급 되었는지 확인하는 함수
    function isTicketMinted(uint32 performanceScheduleId, uint32 seatId) external view returns (bool) {
        _requireScheduled(performanceScheduleId);

        PerformanceSchedule storage schedule = _performanceSchedules[performanceScheduleId];
        return schedule.ticketIndices[seatId] != 0;
    }

    // @dev 티켓의 토큰 ID로부터 티켓이 만료되었는지 확인하는 기능을 제공한다.
    function isTicketExpired(uint tokenId) external view returns (bool) {
        _requireScheduled(obtainPerformanceScheduleId(tokenId));

        return _unsafe_isTicketExpired(tokenId);
    }

    function _mintTicket(address receipent, uint32 performanceScheduleId, uint32 seatId) internal {
        _requireScheduled(performanceScheduleId);

        PerformanceSchedule storage schedule = _performanceSchedules[performanceScheduleId];
        require(schedule.ticketIndices[seatId] == 0, '_mintTicket(): ticket was already minted');

        uint tokenId = makeTokenId(performanceScheduleId, seatId);
        _safeMint(receipent, tokenId);

        schedule.tickets.push(Ticket({isUsed: false}));
        schedule.ticketIndices[seatId] = schedule.tickets.length;
    }

    function mintTicket(address receipent, uint32 performanceScheduleId, uint32 seatId) external onlyOwner {
        _mintTicket(receipent, performanceScheduleId, seatId);
    }

    function _unsafe_ticketDefaultContentUriOf(
        uint32 performanceScheduleId,
        uint32 seatId
    ) private view returns (string memory) {
        string memory ticketDefaultContentBaseUrl = _unsafe_ticketDefaultContentBaseUrlOf(performanceScheduleId);
        if (bytes(ticketDefaultContentBaseUrl).length == 0) {
            return "";
        }
        return string(abi.encodePacked(ticketDefaultContentBaseUrl, Strings.toString(seatId), '.json'));
    }

    function _unsafe_ticketDefaultContentBaseUrlOf(uint32 performanceScheduleId) private view returns (string memory) {
        return _performanceSchedules[performanceScheduleId].ticketDefaultContentBaseUrl;
    }

    function _unsafe_ticketSpecialContentBaseUrlOf(uint32 performanceScheduleId) private view returns (string memory) {
        return _performanceSchedules[performanceScheduleId].ticketSpecialContentBaseUrl;
    }

    function getTicketDefaultContentBaseUrl(uint32 performanceScheduleId) external view returns (string memory) {
        _requireScheduled(performanceScheduleId);

        return _unsafe_ticketDefaultContentBaseUrlOf(performanceScheduleId);
    }

    function getTicketSpecialContentBaseUrl(uint32 performanceScheduleId) external view returns (string memory) {
        _requireScheduled(performanceScheduleId);

        return _unsafe_ticketSpecialContentBaseUrlOf(performanceScheduleId);
    }

    function setTicketDefaultContentBaseUrl(
        uint32 performanceScheduleId,
        string memory newTicketDeafultContentBaseUrl
    ) external onlyOwner {
        _requireScheduled(performanceScheduleId);

        _performanceSchedules[performanceScheduleId].ticketDefaultContentBaseUrl = newTicketDeafultContentBaseUrl;
    }

    function setTicketSpecialContentBaseUrl(
        uint32 performanceScheduleId,
        string memory newTicketSpecialContentBaseUrl
    ) external onlyOwner {
        _requireScheduled(performanceScheduleId);

        _performanceSchedules[performanceScheduleId].ticketSpecialContentBaseUrl = newTicketSpecialContentBaseUrl;
    }

    function tokenURI(uint256 tokenId) public view virtual override returns (string memory) {
        _requireMinted(tokenId);

        uint32 performanceScheduleId = obtainPerformanceScheduleId(tokenId);
        uint32 seatId = obtainSeatId(tokenId);

        Ticket storage ticket = _unsafe_ticketRef(performanceScheduleId, seatId);
        if (!_unsafe_isTicketExpired(tokenId) || !ticket.isUsed) {
            return _unsafe_ticketDefaultContentUriOf(performanceScheduleId, seatId);
        }

        string memory ticketSpecialContentBaseUrl = _unsafe_ticketSpecialContentBaseUrlOf(performanceScheduleId);
        if (bytes(ticketSpecialContentBaseUrl).length == 0) {
            return _unsafe_ticketDefaultContentUriOf(performanceScheduleId, seatId);
        }

        return string(abi.encodePacked(ticketSpecialContentBaseUrl, Strings.toString(seatId), '.json'));
    }
}
