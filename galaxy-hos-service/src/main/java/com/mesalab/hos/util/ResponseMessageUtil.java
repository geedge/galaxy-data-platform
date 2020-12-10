package com.mesalab.hos.util;

import com.mesalab.hos.entity.ErrorResponse;

import javax.servlet.http.HttpServletResponse;

public class ResponseMessageUtil {

    public static ErrorResponse getResponseMessage(String code, String hostId, String requestId, String bucketName, String objectName, HttpServletResponse response) {
        ErrorResponse errorResponse = new ErrorResponse();
        String resource = "/" + bucketName + "/" + objectName;
        errorResponse.setHostId(hostId);
        errorResponse.setRequestId(requestId);
        errorResponse.setResource(resource);
        errorResponse.setCode(code);
        switch (code) {
            case "SignatureDoesNotMatch":
                response.setStatus(403);
                errorResponse.setMessage("The request signature we calculated does not match the signature you provided. Check your AWS secret access key and signing method.");
                break;
            case "InvalidAccessKeyId":
                response.setStatus(403);
                errorResponse.setMessage("The access key ID you provided does not exist in our records.");
                break;
            case "NoSuchBucket":
                response.setStatus(404);
                errorResponse.setMessage("The specified bucket does not exist.");
                break;
            case "InventoryFull":
                response.setStatus(400);
                errorResponse.setMessage("The list of tasks has reached its limit.Limit your list to 1000 tasks.");
                break;
            case "InternalError":
                response.setStatus(500);
                errorResponse.setMessage("We encountered an internal error. Please try again.");
                break;
            case "NoSuchKey":
                response.setStatus(404);
                errorResponse.setMessage("The specified key does not exist.");
                break;
            case "MalformedXML":
                response.setStatus(400);
                errorResponse.setMessage("The XML you provided was not well-formed or did not validate against our published schema.");
                break;
            case "BucketAlreadyOwnedByYou":
                response.setStatus(409);
                errorResponse.setMessage("The bucket you tried to create already exists, and you own it.");
                break;
            case "InvalidPart":
                response.setStatus(400);
                errorResponse.setMessage("One or more of the specified parts could not be found. The part might not have been uploaded, or the specified entity tag might not have matched the part's entity tag.");
                break;
            case "InvalidObjectState":
                response.setStatus(403);
                errorResponse.setMessage("The operation is not valid for the current state of the object.the multipart file already exist.If you want to overwrite please delete it first.");
                break;
            case "InlineDataTooLarge":
                response.setStatus(400);
                errorResponse.setMessage("Inline data exceeds the maximum allowed size.");
                break;
            case "InvalidRange":
                response.setStatus(416);
                errorResponse.setMessage("The requested range cannot be satisfied.");
                break;
            case "NoSuchUpload":
                response.setStatus(404);
                errorResponse.setMessage("The specified multipart upload does not exist. The upload ID may be invalid, or the upload may have been aborted or completed.");
                break;
            case "InvalidArgument":
                response.setStatus(400);
                errorResponse.setMessage("Argument must be an integer between 0 and 2147483647.");
                break;
            case "ObjectNotAppendable":
                response.setStatus(409);
                errorResponse.setMessage("This object can not be appended.");
                break;
            case "PositionNotEqualToLength":
                response.setStatus(409);
                errorResponse.setMessage("Appends position error.");
                break;
            default:
                response.setStatus(500);
                errorResponse.setMessage("Unknow Error.");

        }
        return errorResponse;
    }

    public static String getResponseMessage(String code) {
        String message;
        switch (code) {
            case "SignatureDoesNotMatch":
                message = "The request signature we calculated does not match the signature you provided. Check your AWS secret access key and signing method.";
                break;
            case "InvalidAccessKeyId":
                message = "The access key ID you provided does not exist in our records.";
                break;
            case "NoSuchBucket":
                message = "The specified bucket does not exist.";
                break;
            case "InventoryFull":
                message = "The list of tasks has reached its limit.Limit your list to 1000 tasks.";
                break;
            case "InternalError":
                message = "We encountered an internal error. Please try again.";
                break;
            case "NoSuchKey":
                message = "The specified key does not exist.";
                break;
            case "MalformedXML":
                message = "The XML you provided was not well-formed or did not validate against our published schema.";
                break;
            case "BucketAlreadyOwnedByYou":
                message = "The bucket you tried to create already exists, and you own it.";
                break;
            case "InvalidPart":
                message = "One or more of the specified parts could not be found. The part might not have been uploaded, or the specified entity tag might not have matched the part's entity tag.";
                break;
            case "InvalidObjectState":
                message = "The operation is not valid for the current state of the object.the multipart file already exist.If you want to overwrite please delete it first.";
                break;
            case "InlineDataTooLarge":
                message = "Inline data exceeds the maximum allowed size.";
                break;
            case "InvalidRange":
                message = "The requested range cannot be satisfied.";
                break;
            case "NoSuchUpload":
                message = "The specified multipart upload does not exist. The upload ID may be invalid, or the upload may have been aborted or completed.";
                break;
            case "InvalidArgument":
                message = "Argument must be an integer between 0 and 2147483647.";
                break;
            default:
                message = "Unknow Error.";
        }
        return message;
    }
}
