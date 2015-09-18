var mail = require("nodemailer");
var cfg = require("./config")
var logger = require("./logger");
var smtpTransport = require('nodemailer-smtp-transport');

var transporter = mail.createTransport(smtpTransport({
    host: "smtp.bbcvision.com",
    port: 25,
    "secure": false,
    auth: {
        user: cfg.mail_sender,
        pass: cfg.mail_passwd
    }
}));

var export_obj = {
    "send": function(subject, text, html){
        var obj = {
            "from": cfg.mail_sender,
            "to": cfg.mail_to,
            "subject": subject,
            "text": text,
            "html": html || ""
        }

        transporter.sendMail(obj, function(error, info){
            if(error){
                logger.err(error.stack);
            }else{
                logger.info('Message sent: ' + info.response);
            }
        });
    }
}

module.exports = export_obj;