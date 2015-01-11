/**
	Copyright 2014 [BFR]
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
**/
package org.bfr.querytools.spectrumbridge;

public enum SbiReturnCode
{
	
	Succes,
	MalformedRequest,
	FccIdentifierNotRecognized,
	Reserved,
	DeviceNotRegistered,
	FccDesignatedNoChannelsAvailable,
	CountryCodeNotRecognized,
	DeviceNotEnrolled,
	CountryCodeDoesNotMatchEnrolledCode,
	LocationOutsideRegulatoryDomain,
	AntennaHeightAbove30Meters,
	HaatAbove76Meters,
	FccIdNotInProvidedList,
	InvalidDeviceType,
	ChannelListDataMismatch,
	DeviceTypeNotAllowed,

	// SBI return code out of range.
	GenericError,
	
	// Other return-code parsing issues
	MissingSbiReturnCode,
	MalformedSbiReturnCode

}
